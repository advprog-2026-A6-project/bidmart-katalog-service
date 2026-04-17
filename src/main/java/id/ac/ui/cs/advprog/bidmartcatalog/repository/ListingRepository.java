package id.ac.ui.cs.advprog.bidmartcatalog.repository;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Listing;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.util.List;
import java.util.UUID;


public interface ListingRepository extends JpaRepository<Listing, UUID>, JpaSpecificationExecutor<Listing> {

    @Override
    @EntityGraph(attributePaths = {"category"})
    List<Listing> findAll();

    @Override
    @EntityGraph(attributePaths = {"category"})
    List<Listing> findAll(Specification<Listing> spec);
}