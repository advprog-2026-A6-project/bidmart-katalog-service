package id.ac.ui.cs.advprog.bidmartcatalog;

import id.ac.ui.cs.advprog.bidmartcatalog.dto.ListingBidStatusResponse;
import id.ac.ui.cs.advprog.bidmartcatalog.dto.UpdateListingRequest;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Listing;
import id.ac.ui.cs.advprog.bidmartcatalog.model.ListingStatus;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.CategoryRepository;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.ListingRepository;
import id.ac.ui.cs.advprog.bidmartcatalog.service.ListingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.web.client.RestTemplate;

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

    @InjectMocks
    private ListingService listingService;

    private Listing listing;
    private UUID listingId;

    @BeforeEach
    void setUp() {
        listingId = UUID.randomUUID();

        listing = new Listing();
        listing.setId(listingId);
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

        listingService.cancelListing(listingId);

        assertEquals(ListingStatus.CANCELLED, listing.getStatus());

        verify(listingRepository).save(listing);
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
                () -> listingService.cancelListing(listingId)
        );

        verify(listingRepository, never()).save(any());
    }

    @Test
    void cancelListing_shouldThrow_whenListingNotFound() {

        when(listingRepository.findById(listingId))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> listingService.cancelListing(listingId)
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

        listingService.updateListing(request, listingId);

        assertEquals("New description", listing.getDescription());
        assertEquals("new.jpg", listing.getImageUrl());

        verify(listingRepository).save(listing);
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
                () -> listingService.updateListing(request, listingId));

        verify(listingRepository, never()).save(any());
    }
}
