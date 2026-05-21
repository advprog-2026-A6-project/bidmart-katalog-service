package id.ac.ui.cs.advprog.bidmartcatalog.service;

import id.ac.ui.cs.advprog.bidmartcatalog.config.RabbitConfig;
import id.ac.ui.cs.advprog.bidmartcatalog.dto.ListingStatusChangedEvent;
import id.ac.ui.cs.advprog.bidmartcatalog.model.ListingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ListingAuctionNotifier {

    private final RabbitTemplate rabbitTemplate;

    public void publishStatusChanged(UUID listingId, ListingStatus status) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.AUCTION_LISTING_STATUS_QUEUE,
                new ListingStatusChangedEvent(listingId, status)
        );
    }
}
