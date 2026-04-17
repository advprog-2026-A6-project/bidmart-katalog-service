package id.ac.ui.cs.advprog.bidmartcatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class BidmartCatalogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BidmartCatalogApplication.class, args);
    }

}
