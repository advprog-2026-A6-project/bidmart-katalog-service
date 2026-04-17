package id.ac.ui.cs.advprog.bidmartcatalog;

import id.ac.ui.cs.advprog.bidmartcatalog.config.CategoryDataSeeder;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Category;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryDataSeederTest {

    @Mock
    private CategoryRepository repo;

    @InjectMocks
    private CategoryDataSeeder categoryDataSeeder;

    @Test
    void testRun_WhenDatabaseIsEmpty_ShouldSeedAllCategories() throws Exception {
        // Arrange
        when(repo.count()).thenReturn(0L);

        // Important: return the same object so parent assignment works safely
        when(repo.save(any(Category.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        categoryDataSeeder.run();

        // Assert
        verify(repo, times(18)).save(any(Category.class));
        verify(repo, times(1)).count();
    }

    @Test
    void testRun_WhenDatabaseIsNotEmpty_ShouldNotSaveAnything() throws Exception {
        // Arrange
        when(repo.count()).thenReturn(5L);

        // Act
        categoryDataSeeder.run();

        // Assert
        verify(repo, never()).save(any(Category.class));
        verify(repo, times(1)).count();
    }
}