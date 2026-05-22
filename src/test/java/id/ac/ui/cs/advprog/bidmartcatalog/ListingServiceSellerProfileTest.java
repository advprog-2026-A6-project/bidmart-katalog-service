package id.ac.ui.cs.advprog.bidmartcatalog;

import id.ac.ui.cs.advprog.bidmartcatalog.dto.ListingDTO;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Category;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Listing;
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
import org.springframework.http.HttpMethod;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import id.ac.ui.cs.advprog.bidmartcatalog.dto.SellerPublicProfileDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListingServiceSellerProfileTest {

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

    @BeforeEach
    void setUp() {
        listingId = UUID.randomUUID();
        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName("Books");

        listing = new Listing();
        listing.setId(listingId);
        listing.setTitle("Novel");
        listing.setCategory(category);

        ReflectionTestUtils.setField(listingService, "authServiceUrl", "http://localhost:8081/api/internal/users/");
        ReflectionTestUtils.setField(listingService, "authInternalToken", "test-internal-token");
    }

    @Test
    void getListingById_blankSellerId_skipsAuthCall() {
        listing.setSellerId("  ");
        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));

        ListingDTO dto = listingService.getListingById(listingId);

        assertEquals("Novel", dto.getTitle());
        assertNull(dto.getSellerName());
        verify(restTemplate, never()).exchange(
                anyString(), eq(HttpMethod.GET), any(), eq(SellerPublicProfileDTO.class));
    }

    @Test
    void getAllListings_authServiceFailure_returnsListingWithoutSellerName() {
        listing.setSellerId("seller-9");
        when(listingRepository.findAll()).thenReturn(List.of(listing));

        List<ListingDTO> results = listingService.getAllListings();

        assertEquals(1, results.size());
        assertNull(results.get(0).getSellerName());
        verify(restTemplate, never()).exchange(
                anyString(), eq(HttpMethod.GET), any(), eq(SellerPublicProfileDTO.class));
    }
}
