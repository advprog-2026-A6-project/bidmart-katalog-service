package id.ac.ui.cs.advprog.bidmartcatalog;

import id.ac.ui.cs.advprog.bidmartcatalog.dto.ListingBidStatusResponse;
import id.ac.ui.cs.advprog.bidmartcatalog.dto.UpdateListingRequest;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Listing;
import id.ac.ui.cs.advprog.bidmartcatalog.model.ListingStatus;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.CategoryRepository;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.ListingRepository;
import id.ac.ui.cs.advprog.bidmartcatalog.service.ListingAuctionNotifier;
import id.ac.ui.cs.advprog.bidmartcatalog.service.ListingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListingServiceTestTwo {

    @Mock
    private ListingRepository listingRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ListingAuctionNotifier listingAuctionNotifier;

    @InjectMocks
    private ListingService listingService;

    private Listing listing;
    private UUID listingId;
    private static final String SELLER_ID = "seller-42";

    @BeforeEach
    void setUp() {
        listingId = UUID.randomUUID();

        listing = new Listing();
        listing.setId(listingId);
        listing.setSellerId(SELLER_ID);
        listing.setStatus(ListingStatus.ACTIVE);
        listing.setDescription("Old description");
        listing.setImageUrl("old.jpg");
    }

    @Test
    void cancelListing_shouldCancel_whenNoBids() {

        when(listingRepository.findById(listingId))
                .thenReturn(Optional.of(listing));

        ListingBidStatusResponse response =
                new ListingBidStatusResponse(listingId, false, 0);

        when(restTemplate.getForObject(
                anyString(),
                eq(ListingBidStatusResponse.class)
        )).thenReturn(response);

        listingService.cancelListing(listingId, SELLER_ID);

        assertEquals(ListingStatus.CANCELLED, listing.getStatus());

        verify(listingRepository).save(listing);
        verify(listingAuctionNotifier).publishStatusChanged(listingId, ListingStatus.CANCELLED);
    }

    @Test
    void cancelListing_shouldThrow_whenHasBids() {

        when(listingRepository.findById(listingId))
                .thenReturn(Optional.of(listing));

        ListingBidStatusResponse response =
                new ListingBidStatusResponse(listingId, true, 3);

        when(restTemplate.getForObject(
                anyString(),
                eq(ListingBidStatusResponse.class)
        )).thenReturn(response);

        assertThrows(
                IllegalStateException.class,
                () -> listingService.cancelListing(listingId, SELLER_ID)
        );

        verify(listingRepository, never()).save(any());
        verify(listingAuctionNotifier, never()).publishStatusChanged(any(), any());
    }

    @Test
    void cancelListing_shouldThrow_whenListingNotFound() {

        when(listingRepository.findById(listingId))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> listingService.cancelListing(listingId, SELLER_ID)
        );
    }

    @Test
    void shouldUpdateListingWhenNoBidsExist() {

        UpdateListingRequest request = new UpdateListingRequest();
        request.setDescription("New description");
        request.setImageUrl("new.jpg");

        when(listingRepository.findById(listingId))
                .thenReturn(Optional.of(listing));

        ListingBidStatusResponse response =
                new ListingBidStatusResponse(listingId, false, 0);

        when(restTemplate.getForObject(
                anyString(),
                eq(ListingBidStatusResponse.class)
        )).thenReturn(response);

        when(listingRepository.save(any(Listing.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        listingService.updateListing(request, listingId, SELLER_ID);

        assertEquals("New description", listing.getDescription());
        assertEquals("new.jpg", listing.getImageUrl());

        verify(listingRepository).save(listing);
    }

    @Test
    void updateListing_shouldThrow_whenListingNotFound() {
        UpdateListingRequest request = new UpdateListingRequest();
        request.setDescription("New description");

        when(listingRepository.findById(listingId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> listingService.updateListing(request, listingId, SELLER_ID));

        verify(restTemplate, never()).getForObject(anyString(), eq(ListingBidStatusResponse.class));
    }

    @Test
    void shouldThrowExceptionWhenListingAlreadyHasBids() {

        UpdateListingRequest request = new UpdateListingRequest();
        request.setDescription("New description");

        when(listingRepository.findById(listingId))
                .thenReturn(Optional.of(listing));

        ListingBidStatusResponse response =
                new ListingBidStatusResponse(listingId, true, 1);

        when(restTemplate.getForObject(
                anyString(),
                eq(ListingBidStatusResponse.class)
        )).thenReturn(response);

        assertThrows(IllegalStateException.class,
                () -> listingService.updateListing(request, listingId, SELLER_ID));

        verify(listingRepository, never()).save(any());
    }

    @Test
    void updateListing_shouldThrowBadGateway_whenAuctionBidStatusIsEmpty() {
        UpdateListingRequest request = new UpdateListingRequest();
        request.setDescription("New description");

        when(listingRepository.findById(listingId))
                .thenReturn(Optional.of(listing));
        when(restTemplate.getForObject(anyString(), eq(ListingBidStatusResponse.class)))
                .thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> listingService.updateListing(request, listingId, SELLER_ID));

        assertEquals(502, exception.getStatusCode().value());
        verify(listingRepository, never()).save(any());
    }

    @Test
    void updateListing_shouldThrowForbidden_whenSellerDoesNotOwnListing() {
        UpdateListingRequest request = new UpdateListingRequest();
        request.setDescription("New description");

        when(listingRepository.findById(listingId))
                .thenReturn(Optional.of(listing));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> listingService.updateListing(request, listingId, "other-seller"));

        assertEquals(403, exception.getStatusCode().value());
        verify(restTemplate, never()).getForObject(anyString(), eq(ListingBidStatusResponse.class));
    }

    @Test
    void getListingsBySeller_shouldMergeExactAndPrefixedSellerIds() {
        Listing exactListing = new Listing();
        exactListing.setId(UUID.randomUUID());
        exactListing.setTitle("Exact seller listing");
        exactListing.setSellerId(SELLER_ID);
        exactListing.setStartTime(LocalDateTime.now());

        Listing prefixedListing = new Listing();
        prefixedListing.setId(UUID.randomUUID());
        prefixedListing.setTitle("Prefixed seller listing");
        prefixedListing.setSellerId(SELLER_ID + ",admin");
        prefixedListing.setStartTime(LocalDateTime.now().minusDays(1));

        when(listingRepository.findBySellerIdOrderByStartTimeDesc(SELLER_ID))
                .thenReturn(List.of(exactListing));
        when(listingRepository.findBySellerIdStartingWithOrderByStartTimeDesc(SELLER_ID + ","))
                .thenReturn(List.of(prefixedListing, exactListing));

        List<?> results = listingService.getListingsBySeller(" " + SELLER_ID + ",ignored ");

        assertEquals(2, results.size());
        verify(listingRepository).findBySellerIdOrderByStartTimeDesc(SELLER_ID);
        verify(listingRepository).findBySellerIdStartingWithOrderByStartTimeDesc(SELLER_ID + ",");
    }

    @Test
    void getListingsBySeller_shouldRejectBlankSellerId() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> listingService.getListingsBySeller(" "));

        assertEquals(400, exception.getStatusCode().value());
        verify(listingRepository, never()).findBySellerIdOrderByStartTimeDesc(anyString());
    }
}
