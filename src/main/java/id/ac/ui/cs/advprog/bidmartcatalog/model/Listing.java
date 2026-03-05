package id.ac.ui.cs.advprog.bidmartcatalog.model;


import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "catalog_listings")
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID sellerId;
    private String title;
    private String description;
    private BigDecimal startingPrice;
    private BigDecimal currentPrice;

    @Enumerated(EnumType.STRING)
    private ListingStatus status;
    private Boolean hidden = false;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Version
    private Long version;
}
