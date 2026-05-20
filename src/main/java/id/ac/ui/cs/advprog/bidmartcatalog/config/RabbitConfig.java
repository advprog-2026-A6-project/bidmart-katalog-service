package id.ac.ui.cs.advprog.bidmartcatalog.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    public static final String QUEUE = "catalog.bid-updates";

    @Bean
    public Queue catalogQueue() {
        return new Queue(QUEUE, true);
    }
}
