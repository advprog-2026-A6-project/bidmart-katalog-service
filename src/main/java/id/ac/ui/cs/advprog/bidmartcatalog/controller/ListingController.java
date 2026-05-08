package id.ac.ui.cs.advprog.bidmartcatalog.controller;

import id.ac.ui.cs.advprog.bidmartcatalog.dto.CreateListingRequest;
import id.ac.ui.cs.advprog.bidmartcatalog.dto.ListingDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import id.ac.ui.cs.advprog.bidmartcatalog.model.Listing;
import id.ac.ui.cs.advprog.bidmartcatalog.service.ListingService;


@RestController
@RequestMapping("/listings")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService listingService;

    @PostMapping
    public ResponseEntity<Listing> create(@RequestBody CreateListingRequest request) {
        // change this for milestone 75%
        UUID mockSellerId = UUID.randomUUID();
        Listing listing = listingService.createListing(request, mockSellerId);
        return new ResponseEntity<>(listing, HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ListingDTO>> search(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endBefore
    ) {
        List<ListingDTO> results = listingService.searchListings(
                categoryId, minPrice, maxPrice, keyword, endBefore
        );
        return ResponseEntity.ok(results);
    }

    @GetMapping
    public ResponseEntity<List<ListingDTO>> getAll() {
        List<ListingDTO> listings = listingService.getAllListings();

        return ResponseEntity.ok(listings);
    }

    @GetMapping("/{id}")
    public Listing getById(@PathVariable UUID id) {
        return listingService.getListingById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        listingService.deleteListing(id);
    }

    @PostMapping("/{listingId}/cancel")
    public void cancelListing(@PathVariable UUID listingId) {
        listingService.cancelListing(listingId);
    }


}