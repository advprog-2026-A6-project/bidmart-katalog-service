package id.ac.ui.cs.advprog.bidmartcatalog.controller;

import id.ac.ui.cs.advprog.bidmartcatalog.dto.ListingDTO;
import id.ac.ui.cs.advprog.bidmartcatalog.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/listings/internal")
@RequiredArgsConstructor
public class ListingInternalController {

    private final ListingService listingService;

    @GetMapping("/{listingId}")
    public ResponseEntity<ListingDTO> getListingForIntegration(@PathVariable UUID listingId) {
        return ResponseEntity.ok(listingService.getListingById(listingId));
    }
}
