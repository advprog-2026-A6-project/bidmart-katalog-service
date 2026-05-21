package id.ac.ui.cs.advprog.bidmartcatalog.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    public static final String QUEUE = "catalog.bid-updates";
    public static final String AUCTION_LISTING_STATUS_QUEUE = "auction.listing-status";

    @Bean
    public Queue catalogQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Queue auctionListingStatusQueue() {
        return new Queue(AUCTION_LISTING_STATUS_QUEUE, true);
    }
}
