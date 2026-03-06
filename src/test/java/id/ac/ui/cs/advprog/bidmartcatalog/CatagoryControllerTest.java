package id.ac.ui.cs.advprog.bidmartcatalog;

import id.ac.ui.cs.advprog.bidmartcatalog.controller.CategoryApiController;
import id.ac.ui.cs.advprog.bidmartcatalog.dto.CategoryDTO;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Category;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.CategoryRepository;
import id.ac.ui.cs.advprog.bidmartcatalog.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryApiControllerTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryApiController categoryApiController;

    @Test
    void testGetAll_ShouldReturnListFromRepository() {
        // Arrange
        Category cat = new Category();
        cat.setName("Automotive");
        when(categoryRepository.findAll()).thenReturn(List.of(cat));

        // Act
        List<Category> result = categoryApiController.getAll();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Automotive", result.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testGetCategoryTree_ShouldReturnDtoListFromService() {
        // Arrange
        CategoryDTO dto = new CategoryDTO();
        dto.setName("Electronics");
        dto.setFullPath("Electronics");
        when(categoryService.getTree()).thenReturn(List.of(dto));

        // Act
        ResponseEntity<List<CategoryDTO>> response = categoryApiController.getCategoryTree();

        // Assert
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals("Electronics", response.getBody().get(0).getName());
        verify(categoryService, times(1)).getTree();
    }
}