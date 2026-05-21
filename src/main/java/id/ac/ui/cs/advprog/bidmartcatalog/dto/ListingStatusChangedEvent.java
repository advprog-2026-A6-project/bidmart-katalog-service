package id.ac.ui.cs.advprog.bidmartcatalog.dto;

import id.ac.ui.cs.advprog.bidmartcatalog.model.ListingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListingStatusChangedEvent {
    private UUID listingId;
    private ListingStatus status;
}
