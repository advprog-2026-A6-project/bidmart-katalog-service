package id.ac.ui.cs.advprog.bidmartcatalog.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Queue;

import static org.junit.jupiter.api.Assertions.*;

class RabbitConfigTest {

    @Test
    void catalogQueue_isDurableWithExpectedName() {
        RabbitConfig config = new RabbitConfig();

        Queue queue = config.catalogQueue();

        assertEquals(RabbitConfig.QUEUE, queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void auctionListingStatusQueue_isDurableWithExpectedName() {
        RabbitConfig config = new RabbitConfig();

        Queue queue = config.auctionListingStatusQueue();

        assertEquals(RabbitConfig.AUCTION_LISTING_STATUS_QUEUE, queue.getName());
        assertTrue(queue.isDurable());
    }
}
