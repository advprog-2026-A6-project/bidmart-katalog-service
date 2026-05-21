package id.ac.ui.cs.advprog.bidmartcatalog.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "catalog_listings")
@Setter
@Getter
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String sellerId;
    private String title;
    private String description;
    private BigDecimal startingPrice;
    private BigDecimal reservePrice;
    private BigDecimal currentPrice;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private ListingStatus status;
    private Boolean hidden = false;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
