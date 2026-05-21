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
    private BigDecimal currentPrice;
    private String imageUrl;
    private ListingStatus status;
    private String sellerId;
    private String sellerName;
    private String sellerBio;
    private String sellerProfilePictureUrl;
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
                .currentPrice(listing.getCurrentPrice())
                .imageUrl(listing.getImageUrl())
                .status(listing.getStatus())
                .sellerId(listing.getSellerId())
                .startTime(listing.getStartTime())
                .endTime(listing.getEndTime())

                .categoryId(listing.getCategory() != null ? listing.getCategory().getId() : null)
                .categoryName(listing.getCategory() != null ? listing.getCategory().getName() : "Uncategorized")
                .build();
    }

    public static ListingDTO fromEntity(Listing listing, SellerPublicProfileDTO sellerProfile) {
        ListingDTO dto = fromEntity(listing);
        if (sellerProfile != null) {
            dto.setSellerName(sellerProfile.getName());
            dto.setSellerBio(sellerProfile.getBio());
            dto.setSellerProfilePictureUrl(sellerProfile.getProfilePictureUrl());
        }
        return dto;
    }
}
