package id.ac.ui.cs.advprog.bidmartcatalog;

import id.ac.ui.cs.advprog.bidmartcatalog.dto.ListingBidStatusResponse;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ListingBidStatusResponseTest {

    @Test
    void testGetterSetterAndConstructor() {
        UUID id = UUID.randomUUID();

        ListingBidStatusResponse response =
                new ListingBidStatusResponse(id, true, 5);

        assertEquals(id, response.getListingId());
        assertTrue(response.isHasBids());
        assertEquals(5, response.getBidCount());

        UUID newId = UUID.randomUUID();

        response.setListingId(newId);
        response.setHasBids(false);
        response.setBidCount(10);

        assertEquals(newId, response.getListingId());
        assertFalse(response.isHasBids());
        assertEquals(10, response.getBidCount());
    }
}