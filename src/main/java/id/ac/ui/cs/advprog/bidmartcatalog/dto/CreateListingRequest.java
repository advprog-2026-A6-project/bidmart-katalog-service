package id.ac.ui.cs.advprog.bidmartcatalog.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateListingRequest {
    private String title;
    private String description;
    private BigDecimal startingPrice;
    private BigDecimal currentPrice;
    private String imageUrl;
    private UUID categoryId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}