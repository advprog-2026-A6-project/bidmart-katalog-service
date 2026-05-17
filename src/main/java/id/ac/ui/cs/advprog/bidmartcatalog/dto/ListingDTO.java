package id.ac.ui.cs.advprog.bidmartcatalog.dto;

import id.ac.ui.cs.advprog.bidmartcatalog.model.Listing;
import id.ac.ui.cs.advprog.bidmartcatalog.model.ListingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListingDTO {
    private UUID id;
    private String title;
    private String description;
    private BigDecimal startingPrice;
    private BigDecimal reservePrice;
    private String imageUrl;
    private ListingStatus status;
    private UUID sellerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Flattened Category Info
    private UUID categoryId;
    private String categoryName;

    public static ListingDTO fromEntity(Listing listing) {
        return ListingDTO.builder()
                .id(listing.getId())
                .title(listing.getTitle())
                .description(listing.getDescription())
                .startingPrice(listing.getStartingPrice())
                .reservePrice(listing.getReservePrice())
                .imageUrl(listing.getImageUrl())
                .status(listing.getStatus())
                .sellerId(listing.getSellerId())
                .startTime(listing.getStartTime())
                .endTime(listing.getEndTime())

                .categoryId(listing.getCategory() != null ? listing.getCategory().getId() : null)
                .categoryName(listing.getCategory() != null ? listing.getCategory().getName() : "Uncategorized")
                .build();
    }
}