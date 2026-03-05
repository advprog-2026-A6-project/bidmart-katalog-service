package id.ac.ui.cs.advprog.bidmartcatalog.service;

import org.springframework.stereotype.Service;
import java.util.List;
import lombok.RequiredArgsConstructor;

import id.ac.ui.cs.advprog.bidmartcatalog.model.Listing;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.ListingRepository;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;

    public Listing createListing(Listing listing) {
        return listingRepository.save(listing);
    }

    public List<Listing> getAllListings() {
        return listingRepository.findAll();
    }

    public Listing getListingById(Long id) {
        return listingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Listing not found"));
    }

    public void deleteListing(Long id) {
        listingRepository.deleteById(id);
    }
}