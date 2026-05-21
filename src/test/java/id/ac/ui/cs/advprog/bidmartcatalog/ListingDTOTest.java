package id.ac.ui.cs.advprog.bidmartcatalog;

import id.ac.ui.cs.advprog.bidmartcatalog.dto.ListingDTO;
import id.ac.ui.cs.advprog.bidmartcatalog.dto.SellerPublicProfileDTO;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Category;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Listing;
import id.ac.ui.cs.advprog.bidmartcatalog.model.ListingStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ListingDTOTest {

    @Test
    void fromEntity_withoutCategory_usesUncategorized() {
        Listing listing = baseListing();
        listing.setCategory(null);

        ListingDTO dto = ListingDTO.fromEntity(listing);

        assertNull(dto.getCategoryId());
        assertEquals("Uncategorized", dto.getCategoryName());
    }

    @Test
    void fromEntity_withSellerProfile_enrichesSellerFields() {
        Listing listing = baseListing();
        SellerPublicProfileDTO profile = new SellerPublicProfileDTO(
                1L, "Alice", "Bio", "https://example.com/p.jpg"
        );

        ListingDTO dto = ListingDTO.fromEntity(listing, profile);

        assertEquals("Alice", dto.getSellerName());
        assertEquals("Bio", dto.getSellerBio());
        assertEquals("https://example.com/p.jpg", dto.getSellerProfilePictureUrl());
    }

    @Test
    void fromEntity_withNullSellerProfile_leavesSellerFieldsUnset() {
        Listing listing = baseListing();

        ListingDTO dto = ListingDTO.fromEntity(listing, null);

        assertNull(dto.getSellerName());
        assertNull(dto.getSellerBio());
        assertNull(dto.getSellerProfilePictureUrl());
    }

    private Listing baseListing() {
        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName("Electronics");

        Listing listing = new Listing();
        listing.setId(UUID.randomUUID());
        listing.setTitle("Phone");
        listing.setDescription("Like new");
        listing.setStartingPrice(new BigDecimal("100"));
        listing.setReservePrice(new BigDecimal("200"));
        listing.setCurrentPrice(new BigDecimal("150"));
        listing.setImageUrl("img.png");
        listing.setStatus(ListingStatus.ACTIVE);
        listing.setSellerId("seller-1");
        listing.setStartTime(LocalDateTime.now());
        listing.setEndTime(LocalDateTime.now().plusDays(1));
        listing.setCategory(category);
        return listing;
    }
}
