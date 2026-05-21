package id.ac.ui.cs.advprog.bidmartcatalog;

import id.ac.ui.cs.advprog.bidmartcatalog.model.Category;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Listing;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.CategoryRepository;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.ListingRepository;
import id.ac.ui.cs.advprog.bidmartcatalog.service.ListingService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ListingServiceSearchTest {

    @Mock
    private ListingRepository listingRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ListingService listingService;

    private Listing sampleListing;
    private UUID categoryId;

    @BeforeEach
    void setUp() {
        categoryId = UUID.randomUUID();
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");

        sampleListing = new Listing();
        sampleListing.setId(UUID.randomUUID());
        sampleListing.setTitle("Camera");
        sampleListing.setCategory(category);
        sampleListing.setSellerId(null);

        ReflectionTestUtils.setField(listingService, "authServiceUrl", "http://localhost:8081/api/internal/users/");
        ReflectionTestUtils.setField(listingService, "authInternalToken", "test-internal-token");
    }

    @Test
    void searchListings_noFilters_doesNotQueryDescendants() {
        captureSearch(null, null, null, null, null);

        verify(categoryRepository, never()).findAllDescendantIds(any());
    }

    @Test
    void searchListings_withAllFilters_executesPredicateLambdas() {
        when(categoryRepository.findAllDescendantIds(categoryId))
                .thenReturn(List.of(categoryId));

        Specification<Listing> spec = captureSearch(
                categoryId,
                new BigDecimal("100"),
                new BigDecimal("500"),
                "camera",
                LocalDateTime.now().plusDays(2)
        );

        invokeSpecification(spec);
        verify(categoryRepository).findAllDescendantIds(categoryId);
    }

    @Test
    void searchListings_blankKeyword_skipsKeywordPredicate() {
        Specification<Listing> spec = captureSearch(null, null, null, "   ", null);

        invokeSpecification(spec);
    }

    @Test
    void searchListings_onlyMinPrice_executesMinPricePredicate() {
        Specification<Listing> spec = captureSearch(null, new BigDecimal("50"), null, null, null);

        invokeSpecification(spec);
    }

    @Test
    void searchListings_onlyEndBefore_executesEndBeforePredicate() {
        Specification<Listing> spec = captureSearch(
                null, null, null, null, LocalDateTime.now().plusDays(1)
        );

        invokeSpecification(spec);
    }

    @SuppressWarnings("unchecked")
    private Specification<Listing> captureSearch(
            UUID category,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String keyword,
            LocalDateTime endBefore
    ) {
        ArgumentCaptor<Specification<Listing>> captor = ArgumentCaptor.forClass(Specification.class);
        when(listingRepository.findAll(captor.capture())).thenReturn(List.of(sampleListing));

        listingService.searchListings(category, minPrice, maxPrice, keyword, endBefore);

        return captor.getValue();
    }

    @SuppressWarnings("unchecked")
    private void invokeSpecification(Specification<Listing> specification) {
        Root<Listing> root = mock(Root.class);
        CriteriaQuery<Object> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        Predicate predicate = mock(Predicate.class);

        lenient().when(cb.conjunction()).thenReturn(predicate);
        lenient().when(cb.and(any(), any())).thenReturn(predicate);
        lenient().when(cb.or(any(), any())).thenReturn(predicate);
        lenient().when(cb.greaterThanOrEqualTo(any(Expression.class), any(Comparable.class))).thenReturn(predicate);
        lenient().when(cb.lessThanOrEqualTo(any(Expression.class), any(Comparable.class))).thenReturn(predicate);
        lenient().when(cb.like(any(Expression.class), anyString())).thenReturn(predicate);
        lenient().when(cb.lower(any(Expression.class))).thenReturn(mock(Expression.class));

        Path<Object> path = mock(Path.class);
        lenient().when(root.get(anyString())).thenReturn(path);
        lenient().when(path.get(anyString())).thenReturn(path);
        lenient().when(path.in(anyCollection())).thenReturn(predicate);

        specification.toPredicate(root, query, cb);
    }
}
