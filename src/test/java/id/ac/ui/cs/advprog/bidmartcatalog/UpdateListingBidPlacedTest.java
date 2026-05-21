package id.ac.ui.cs.advprog.bidmartcatalog;

import id.ac.ui.cs.advprog.bidmartcatalog.dto.BidPlacedEvent;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Listing;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.ListingRepository;
import id.ac.ui.cs.advprog.bidmartcatalog.service.ListingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateListingPriceListener")
class UpdateListingBidPlacedTest {

    @Mock
    private ListingRepository listingRepository;

    @InjectMocks
    private ListingService listener;

    private Listing sampleListing;
    private UUID sampleId;
    private BidPlacedEvent sampleBidPlacedEvent;
    private BigDecimal sampleBidAmount;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();

        sampleBidAmount = new BigDecimal("150000");

        sampleListing = new Listing();
        sampleListing.setId(sampleId);
        sampleListing.setReservePrice(new BigDecimal("100000"));
        sampleListing.setCurrentPrice(new BigDecimal("100000"));

        sampleBidPlacedEvent = new BidPlacedEvent();
        sampleBidPlacedEvent.setListingId(sampleId);
        sampleBidPlacedEvent.setBidAmount(sampleBidAmount);
    }

    // =========================================================================
    // Happy Path
    // =========================================================================

    @Nested
    @DisplayName("Happy Path")
    class HappyPath {

        @Test
        @DisplayName("should update price when listing exists")
        void shouldUpdatePriceWhenListingExists() {

            // Arrange
            when(listingRepository.findById(sampleId))
                    .thenReturn(Optional.of(sampleListing));

            // Act
            listener.handleBidPlacedEvent(sampleBidPlacedEvent);

            // Assert
            ArgumentCaptor<Listing> captor =
                    ArgumentCaptor.forClass(Listing.class);

            verify(listingRepository).save(captor.capture());

            Listing saved = captor.getValue();

            assertThat(saved.getCurrentPrice())
                    .isEqualByComparingTo(sampleBidAmount);
        }

        @Test
        @DisplayName("should persist the correct listing entity")
        void shouldSaveCorrectListingEntity() {

            // Arrange
            when(listingRepository.findById(sampleId))
                    .thenReturn(Optional.of(sampleListing));

            // Act
            listener.handleBidPlacedEvent(sampleBidPlacedEvent);

            // Assert
            ArgumentCaptor<Listing> captor =
                    ArgumentCaptor.forClass(Listing.class);

            verify(listingRepository, times(1))
                    .save(captor.capture());

            assertThat(captor.getValue().getId())
                    .isEqualTo(sampleId);
        }
    }

    // =========================================================================
    // Unhappy Path
    // =========================================================================

    @Nested
    @DisplayName("Unhappy Path")
    class UnhappyPath {

        @Test
        @DisplayName("should throw exception when listing does not exist")
        void shouldThrowWhenListingNotFound() {

            // Arrange
            when(listingRepository.findById(sampleId))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThatThrownBy(() ->
                    listener.handleBidPlacedEvent(sampleBidPlacedEvent)
            )
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining(sampleId.toString());

            verify(listingRepository, never()).save(any());
        }

        @Test
        @DisplayName("should never call save when listing does not exist")
        void shouldNotSaveWhenListingNotFound() {

            // Arrange
            when(listingRepository.findById(sampleId))
                    .thenReturn(Optional.empty());

            // Act
            try {
                listener.handleBidPlacedEvent(sampleBidPlacedEvent);
            } catch (IllegalArgumentException ignored) {
            }

            // Assert
            verify(listingRepository, never()).save(any());
        }
    }
}
