package id.ac.ui.cs.advprog.bidmartcatalog;

import id.ac.ui.cs.advprog.bidmartcatalog.config.RabbitConfig;
import id.ac.ui.cs.advprog.bidmartcatalog.dto.ListingStatusChangedEvent;
import id.ac.ui.cs.advprog.bidmartcatalog.model.ListingStatus;
import id.ac.ui.cs.advprog.bidmartcatalog.service.ListingAuctionNotifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ListingAuctionNotifierTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ListingAuctionNotifier notifier;

    @Test
    void publishStatusChanged_sendsToAuctionQueue() {
        UUID listingId = UUID.randomUUID();

        notifier.publishStatusChanged(listingId, ListingStatus.CANCELLED);

        ArgumentCaptor<ListingStatusChangedEvent> captor =
                ArgumentCaptor.forClass(ListingStatusChangedEvent.class);
        verify(rabbitTemplate).convertAndSend(
                org.mockito.ArgumentMatchers.eq(RabbitConfig.AUCTION_LISTING_STATUS_QUEUE),
                captor.capture()
        );
        assertEquals(listingId, captor.getValue().getListingId());
        assertEquals(ListingStatus.CANCELLED, captor.getValue().getStatus());
    }
}
