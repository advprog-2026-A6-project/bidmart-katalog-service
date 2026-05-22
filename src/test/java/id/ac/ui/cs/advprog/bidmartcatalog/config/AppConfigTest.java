package id.ac.ui.cs.advprog.bidmartcatalog.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppConfigTest {

    @Test
    void restTemplateBean_isCreated() {
        AppConfig config = new AppConfig();

        RestTemplate restTemplate = config.restTemplate(new RestTemplateBuilder());

        assertNotNull(restTemplate);
    }
}
