package id.ac.ui.cs.advprog.bidmartcatalog.controller;

import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
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
    public Listing create(@RequestBody Listing listing) {
        return listingService.createListing(listing);
    }

    @GetMapping
    public List<Listing> getAll() {
        return listingService.getAllListings();
    }

    @GetMapping("/{id}")
    public Listing getById(@PathVariable UUID id) {
        return listingService.getListingById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        listingService.deleteListing(id);
    }
}