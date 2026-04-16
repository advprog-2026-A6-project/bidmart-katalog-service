package id.ac.ui.cs.advprog.bidmartcatalog.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Listing;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.util.UUID;


public interface ListingRepository extends JpaRepository<Listing, UUID>, JpaSpecificationExecutor<Listing> {

}