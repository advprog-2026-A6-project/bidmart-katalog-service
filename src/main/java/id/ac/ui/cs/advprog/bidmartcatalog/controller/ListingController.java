package id.ac.ui.cs.advprog.bidmartcatalog.controller;

import id.ac.ui.cs.advprog.bidmartcatalog.dto.CreateListingRequest;
import id.ac.ui.cs.advprog.bidmartcatalog.dto.ListingDTO;
import id.ac.ui.cs.advprog.bidmartcatalog.dto.UpdateListingRequest;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<Listing> create(
            @RequestBody CreateListingRequest request,
            @RequestHeader("X-User-Id") String sellerId
    ) {
        Listing listing = listingService.createListing(request, sellerId);
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

    @GetMapping("/mine")
    public ResponseEntity<List<ListingDTO>> getMyListings(
            @RequestHeader("X-User-Id") String sellerId
    ) {
        return ResponseEntity.ok(listingService.getListingsBySeller(sellerId));
    }

    @GetMapping
    public ResponseEntity<List<ListingDTO>> getAll() {
        List<ListingDTO> listings = listingService.getAllListings();

        return ResponseEntity.ok(listings);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListingDTO> updateListing(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateListingRequest request,
            @RequestHeader("X-User-Id") String sellerId
    ) {
        return ResponseEntity.ok(listingService.updateListing(request, id, sellerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingDTO> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(listingService.getListingById(id));
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") String sellerId
    ) {
        listingService.deleteListing(id, sellerId);
    }

    @PostMapping("/{listingId}/cancel")
    public void cancelListing(
            @PathVariable UUID listingId,
            @RequestHeader("X-User-Id") String sellerId
    ) {
        listingService.cancelListing(listingId, sellerId);
    }


}
