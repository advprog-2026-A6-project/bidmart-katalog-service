package id.ac.ui.cs.advprog.bidmartcatalog.controller;

import id.ac.ui.cs.advprog.bidmartcatalog.dto.CategoryDTO;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.CategoryRepository;
import id.ac.ui.cs.advprog.bidmartcatalog.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

import id.ac.ui.cs.advprog.bidmartcatalog.model.Category;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor // Lombok creates constructor for the Repository
public class CategoryApiController {

    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @GetMapping("/tree")
    public ResponseEntity<List<CategoryDTO>> getCategoryTree() {
        return ResponseEntity.ok(categoryService.getTree());
    }
}