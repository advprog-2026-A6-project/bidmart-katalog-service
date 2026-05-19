package id.ac.ui.cs.advprog.bidmartcatalog.service;

import id.ac.ui.cs.advprog.bidmartcatalog.config.RabbitConfig;
import id.ac.ui.cs.advprog.bidmartcatalog.dto.*;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Category;
import id.ac.ui.cs.advprog.bidmartcatalog.model.ListingStatus;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import id.ac.ui.cs.advprog.bidmartcatalog.model.Listing;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.ListingRepository;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;
    private final CategoryRepository categoryRepository;
    private final RestTemplate restTemplate;

    @Value("${service.auction.url}")
    private String auctionServiceUrl;

    @Transactional
    public Listing createListing(CreateListingRequest request, UUID sellerId) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Listing listing = new Listing();
        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        listing.setStartingPrice(request.getStartingPrice());
        listing.setReservePrice(request.getReservePrice());
        listing.setCurrentPrice(request.getCurrentPrice());
        listing.setImageUrl(request.getImageUrl());
        listing.setStartTime(request.getStartTime());
        listing.setEndTime(request.getEndTime());

        listing.setCategory(category);
        listing.setSellerId(sellerId);
        listing.setStatus(ListingStatus.ACTIVE);

        return listingRepository.save(listing);
    }

    @Transactional(readOnly = true)
    public List<ListingDTO> getAllListings() {
        return listingRepository.findAll().stream()
                .map(ListingDTO::fromEntity)
                .toList();
    }

    public Listing getListingById(UUID id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));
    }

    public ListingDTO updateListing(UpdateListingRequest request, UUID listingId) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        String url = auctionServiceUrl + listingId + "/bids/status";
        ListingBidStatusResponse response = restTemplate.getForObject(url, ListingBidStatusResponse.class);
        assert response != null;

        if (response.isHasBids()) {
            throw new IllegalStateException("Gagal memperbarui: Listing sudah memiliki penawaran.");
        }

        listing.setDescription(request.getDescription());
        listing.setImageUrl(request.getImageUrl());
        listingRepository.save(listing);

        return ListingDTO.fromEntity(listing);
    }

    public void deleteListing(UUID id) {
        listingRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ListingDTO> searchListings(
            UUID categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String keyword,
            LocalDateTime endBefore) {

        Specification<Listing> spec = (root, query, cb) -> cb.conjunction();

        if (categoryId != null) {

            List<UUID> allTargetIds = categoryRepository.findAllDescendantIds(categoryId);

            spec = spec.and((root, query, cb) ->
                    root.get("category").get("id").in(allTargetIds));
        }

        if (minPrice != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("reservePrice"), minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("reservePrice"), maxPrice));
        }

        if (keyword != null && !keyword.isBlank()) {
            String pattern = "%" + keyword.toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("title")), pattern),
                            cb.like(cb.lower(root.get("description")), pattern)
                    )
            );
        }

        if (endBefore != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("endTime"), endBefore));
        }


        return listingRepository.findAll(spec).stream()
                .map(ListingDTO::fromEntity)
                .toList();
    }

    public void cancelListing(UUID listingId) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new RuntimeException("Listing not found"));

        String url = auctionServiceUrl + listingId + "/bids/status";
        ListingBidStatusResponse response = restTemplate.getForObject(url, ListingBidStatusResponse.class);
        assert response != null;
        boolean hasBids = response.isHasBids();

        if (hasBids) {
            // throw error
            throw new IllegalStateException("Gagal membatalkan: Listing sudah memiliki penawaran.");
        } else {
            listing.setStatus(ListingStatus.CANCELLED);
            listingRepository.save(listing);
        }
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void handleBidPlacedEvent(BidPlacedEvent event) {
        listingRepository.findById(event.getListingId())
                .ifPresent(listing -> {
                    listing.setCurrentPrice(event.getBidAmount());
                    listingRepository.save(listing);
                });
    }

}