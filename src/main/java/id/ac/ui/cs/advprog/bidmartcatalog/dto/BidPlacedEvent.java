package id.ac.ui.cs.advprog.bidmartcatalog.dto;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;

@Data
public class BidPlacedEvent {
    private UUID listingId;
    private BigDecimal bidAmount;
}