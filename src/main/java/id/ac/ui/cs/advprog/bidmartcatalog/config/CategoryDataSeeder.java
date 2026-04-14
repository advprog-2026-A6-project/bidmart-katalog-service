package id.ac.ui.cs.advprog.bidmartcatalog.config;

import id.ac.ui.cs.advprog.bidmartcatalog.model.Category;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryDataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            seedCategories();
        }
    }

    private void seedCategories() {
        createCategoryWithSubs("Electronics", Arrays.asList("Smartphones", "Laptops"));
        createCategoryWithSubs("Fashion", Arrays.asList("Clothing", "Shoes"));
        createCategoryWithSubs("Home & Living", Arrays.asList("Furniture", "Kitchenware"));
        createCategoryWithSubs("Hobbies & Collectibles", Arrays.asList("Action Figures", "Trading Cards"));
        createCategoryWithSubs("Automotive", Arrays.asList("Car Parts", "Motorcycle Accessories"));
        createCategoryWithSubs("Others", Arrays.asList("General", "Misc Services"));

        System.out.println("Marketplace categories intialized.");
    }

    private void createCategoryWithSubs(String parentName, List<String> subNames) {
        Category parent = new Category();
        parent.setName(parentName);
        parent = categoryRepository.save(parent);

        for (String subName : subNames) {
            Category sub = new Category();
            sub.setName(subName);
            sub.setParent(parent);
            categoryRepository.save(sub);
        }
    }
}