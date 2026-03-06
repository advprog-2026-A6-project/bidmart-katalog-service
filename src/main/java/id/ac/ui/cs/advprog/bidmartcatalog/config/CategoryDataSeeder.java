package id.ac.ui.cs.advprog.bidmartcatalog.config;

import id.ac.ui.cs.advprog.bidmartcatalog.model.Category;
import id.ac.ui.cs.advprog.bidmartcatalog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryDataSeeder implements CommandLineRunner {

    private final CategoryRepository repo;

    @Override
    public void run(String... args) {
        if (repo.count() == 0) {
            Category electronics = new Category();
            electronics.setName("Elektronik");
            repo.save(electronics);

            Category phone = new Category();
            phone.setName("Handphone");
            phone.setParent(electronics);
            repo.save(phone);

            Category smartphone = new Category();
            smartphone.setName("Smartphone");
            smartphone.setParent(phone);
            repo.save(smartphone);

            System.out.println("Database seeded with initial categories!");
        } else {
            System.out.println("Categories already exist, skipping seed.");
        }
    }
}