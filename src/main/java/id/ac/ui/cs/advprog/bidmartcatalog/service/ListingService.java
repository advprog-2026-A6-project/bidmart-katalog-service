package id.ac.ui.cs.advprog.bidmartcatalog.service;

import id.ac.ui.cs.advprog.bidmartcatalog.dto.CreateListingRequest;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Category;
import id.ac.ui.cs.advprog.bidmartcatalog.model.ListingStatus;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import id.ac.ui.cs.advprog.bidmartcatalog.model.Listing;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.ListingRepository;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Listing createListing(CreateListingRequest request, UUID sellerId) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Validation: memastikan tidak memilih category parent.
        if (!category.getChildren().isEmpty()) {
            throw new RuntimeException("Please select a more specific sub-category.");
        }

        Listing listing = new Listing();
        listing.setTitle(request.getTitle());
        listing.setDescription(request.getDescription());
        listing.setStartingPrice(request.getStartingPrice());
        listing.setCurrentPrice(request.getStartingPrice());
        listing.setImageUrl(request.getImageUrl());
        listing.setStartTime(request.getStartTime());
        listing.setEndTime(request.getEndTime());

        listing.setCategory(category);

        listing.setSellerId(sellerId);
        listing.setStatus(ListingStatus.ACTIVE);

        return listingRepository.save(listing);
    }

    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    public Listing getListingById(UUID id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));
    }

    public void deleteListing(UUID id) {
        listingRepository.deleteById(id);
    }

    public List<Listing> searchListings(UUID categoryId) {
        Specification<Listing> spec = (root, query, cb) -> cb.conjunction();

        if (categoryId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId));
        }

        return listingRepository.findAll(spec);
    }

}