package id.ac.ui.cs.advprog.bidmartcatalog;

import id.ac.ui.cs.advprog.bidmartcatalog.dto.CreateListingRequest;
import id.ac.ui.cs.advprog.bidmartcatalog.dto.ListingDTO;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Category;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Listing;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.CategoryRepository;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.ListingRepository;
import id.ac.ui.cs.advprog.bidmartcatalog.service.ListingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListingServiceTest {

    @Mock
    private ListingRepository listingRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ListingService listingService;

    private Listing sampleListing;
    private Category sampleCategory;
    private UUID sampleId;
    private UUID categoryId;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        categoryId = UUID.randomUUID();

        sampleCategory = new Category();
        sampleCategory.setId(categoryId);
        sampleCategory.setName("Electronics");

        sampleListing = new Listing();
        sampleListing.setId(sampleId);
        sampleListing.setTitle("Vintage Camera");
        sampleListing.setReservePrice(new BigDecimal("100000"));
        sampleListing.setCategory(sampleCategory);
    }

    @Test
    void testCreateListing_ShouldReturnSavedListing() {
        // Arrange
        CreateListingRequest request = new CreateListingRequest();
        request.setTitle("Vintage Camera");
        request.setDescription("Old but gold");
        request.setStartingPrice(new BigDecimal("100000"));
        request.setImageUrl("img.jpg");
        request.setStartTime(LocalDateTime.now());
        request.setEndTime(LocalDateTime.now().plusDays(1));
        request.setCategoryId(categoryId);

        UUID sellerId = UUID.randomUUID();

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(sampleCategory));

        when(listingRepository.save(any(Listing.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Listing result = listingService.createListing(request, sellerId);

        // Assert
        assertNotNull(result);
        assertEquals("Vintage Camera", result.getTitle());
        assertEquals(sampleCategory, result.getCategory());
        assertEquals(sellerId, result.getSellerId());

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(listingRepository, times(1)).save(any(Listing.class));
    }

    @Test
    void testCreateListing_CategoryNotFound_ShouldThrowException() {
        // Arrange
        CreateListingRequest request = new CreateListingRequest();
        request.setCategoryId(categoryId);

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                listingService.createListing(request, UUID.randomUUID())
        );

        assertEquals("Category not found", exception.getMessage());
    }

    @Test
    void testGetListingById_Success() {
        when(listingRepository.findById(sampleId))
                .thenReturn(Optional.of(sampleListing));

        Listing found = listingService.getListingById(sampleId);

        assertNotNull(found);
        assertEquals("Vintage Camera", found.getTitle());
    }

    @Test
    void testGetListingById_NotFound_ShouldThrowException() {
        when(listingRepository.findById(sampleId))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () ->
                listingService.getListingById(sampleId)
        );

        assertEquals("Listing not found", exception.getMessage());
    }

    @Test
    void testGetAllListings_ShouldReturnList() {
        when(listingRepository.findAll())
                .thenReturn(List.of(sampleListing));

        List<ListingDTO> result = listingService.getAllListings();

        assertEquals(1, result.size());
    }

    @Test
    void testDeleteListing_ShouldCallRepository() {
        listingService.deleteListing(sampleId);

        verify(listingRepository, times(1)).deleteById(sampleId);
    }

    @Test
    void testSearchListings_WithFilters_ShouldReturnResults() {
        // Arrange
        when(categoryRepository.findAllDescendantIds(categoryId))
                .thenReturn(List.of(categoryId));

        when(listingRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(sampleListing));

        // Act
        List<ListingDTO> results = listingService.searchListings(
                categoryId,
                new BigDecimal("50000"),
                new BigDecimal("150000"),
                "camera",
                LocalDateTime.now().plusDays(1)
        );

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());

        verify(categoryRepository, times(1)).findAllDescendantIds(categoryId);
        verify(listingRepository, times(1)).findAll(any(Specification.class));
    }


}