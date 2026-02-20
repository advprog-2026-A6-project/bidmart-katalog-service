package id.ac.ui.cs.advprog.bidmartcatalog;

import id.ac.ui.cs.advprog.bidmartcatalog.controller.ProductController;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Product;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerUnitTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId(UUID.randomUUID());
        product.setProductName("Test Product");
        product.setProductQuantity(5);
    }

    @Test
    void getAllProducts_shouldReturnList() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<Product> result = productController.getAllProducts();

        verify(productRepository).findAll();
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getProductName());
    }

    @Test
    void createProduct_shouldSaveAndReturnProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productController.createProduct(product);

        verify(productRepository).save(product);
        assertNotNull(result.getProductId());
        assertEquals("Test Product", result.getProductName());
        assertEquals(5, result.getProductQuantity());
    }
}