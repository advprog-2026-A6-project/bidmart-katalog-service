package id.ac.ui.cs.advprog.bidmartcatalog.service;

import id.ac.ui.cs.advprog.bidmartcatalog.dto.CategoryDTO;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Category;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> getTree() {
        return categoryRepository.findByParentIsNull().stream()
                .map(this::mapToDTO)
                .toList();
    }

    private CategoryDTO mapToDTO(Category entity) {
        return CategoryDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .fullPath(calculatePath(entity))
                .children(entity.getChildren().stream().map(this::mapToDTO).toList())
                .build();
    }

    private String calculatePath(Category entity) {
        if (entity.getParent() == null) return entity.getName();
        return calculatePath(entity.getParent()) + " > " + entity.getName();
    }
}
