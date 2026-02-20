package id.ac.ui.cs.advprog.bidmartcatalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
}