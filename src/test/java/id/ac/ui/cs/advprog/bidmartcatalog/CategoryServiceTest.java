package id.ac.ui.cs.advprog.bidmartcatalog;

import id.ac.ui.cs.advprog.bidmartcatalog.dto.CategoryDTO;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Category;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.CategoryRepository;
import id.ac.ui.cs.advprog.bidmartcatalog.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category rootCategory;
    private Category childCategory;

    @BeforeEach
    void setUp() {
        // Setup Root: Elektronik
        rootCategory = new Category();
        rootCategory.setId(UUID.randomUUID());
        rootCategory.setName("Elektronik");
        rootCategory.setChildren(new ArrayList<>());
        rootCategory.setParent(null);

        // Setup Child: Handphone
        childCategory = new Category();
        childCategory.setId(UUID.randomUUID());
        childCategory.setName("Handphone");
        childCategory.setChildren(new ArrayList<>());
        childCategory.setParent(rootCategory);

        // Link them
        rootCategory.getChildren().add(childCategory);
    }

    @Test
    void testGetTree_ShouldReturnCorrectHierarchyAndPath() {
        // Arrange: Repository returns only the root
        when(categoryRepository.findByParentIsNull()).thenReturn(List.of(rootCategory));

        // Act
        List<CategoryDTO> result = categoryService.getTree();

        // Assert: Root Level
        assertNotNull(result);
        assertEquals(1, result.size());
        CategoryDTO rootDTO = result.get(0);
        assertEquals("Elektronik", rootDTO.getName());
        assertEquals("Elektronik", rootDTO.getFullPath());

        // Assert: Child Level (Recursion Check)
        assertEquals(1, rootDTO.getChildren().size());
        CategoryDTO childDTO = rootDTO.getChildren().get(0);
        assertEquals("Handphone", childDTO.getName());

        // Assert: Breadcrumb Logic Check
        assertEquals("Elektronik > Handphone", childDTO.getFullPath());
    }

    @Test
    void testGetTree_EmptyDatabase_ShouldReturnEmptyList() {
        when(categoryRepository.findByParentIsNull()).thenReturn(List.of());

        List<CategoryDTO> result = categoryService.getTree();

        assertTrue(result.isEmpty());
    }
}
