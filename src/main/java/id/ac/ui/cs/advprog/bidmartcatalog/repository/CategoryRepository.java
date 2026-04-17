package id.ac.ui.cs.advprog.bidmartcatalog.repository;

import id.ac.ui.cs.advprog.bidmartcatalog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByParentIsNull();

    @Query(value = "WITH RECURSIVE subcategories AS (" +
            "SELECT id FROM catalog_categories WHERE id = :parentId " +
            "UNION ALL " +
            "SELECT c.id FROM catalog_categories c " +
            "INNER JOIN subcategories s ON s.id = c.parent_id" +
            ") SELECT id FROM subcategories", nativeQuery = true)
    List<UUID> findAllDescendantIds(@Param("parentId") UUID parentId);
}