package id.ac.ui.cs.advprog.bidmartcatalog;

import id.ac.ui.cs.advprog.bidmartcatalog.model.Listing;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.ListingRepository;
import id.ac.ui.cs.advprog.bidmartcatalog.service.ListingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListingServiceTest {

    @Mock
    private ListingRepository listingRepository;

    @InjectMocks
    private ListingService listingService;

    private Listing sampleListing;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        sampleListing = new Listing();
        // Assuming your Listing has a setTitle or similar;
        // adjust based on your actual fields
        sampleListing.setTitle("Vintage Camera");
    }

//    @Test
//    void testCreateListing_ShouldReturnSavedListing() {
//        when(listingRepository.save(any(Listing.class))).thenReturn(sampleListing);
//
//        Listing created = listingService.createListing(new Listing());
//
//        assertNotNull(created);
//        assertEquals("Vintage Camera", created.getTitle());
//        verify(listingRepository, times(1)).save(any());
//    }

    @Test
    void testGetListingById_Success() {
        when(listingRepository.findById(sampleId)).thenReturn(Optional.of(sampleListing));

        Listing found = listingService.getListingById(sampleId);

        assertNotNull(found);
        assertEquals("Vintage Camera", found.getTitle());
    }

    @Test
    void testGetListingById_NotFound_ShouldThrowException() {
        when(listingRepository.findById(sampleId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            listingService.getListingById(sampleId);
        });

        assertEquals("Listing not found", exception.getMessage());
    }

    @Test
    void testGetAllListings_ShouldReturnList() {
        when(listingRepository.findAll()).thenReturn(List.of(sampleListing));

        List<Listing> all = listingService.getAllListings();

        assertFalse(all.isEmpty());
        assertEquals(1, all.size());
    }

    @Test
    void testDeleteListing_ShouldCallRepository() {
        doNothing().when(listingRepository).deleteById(sampleId);

        listingService.deleteListing(sampleId);

        verify(listingRepository, times(1)).deleteById(sampleId);
    }
}