package id.ac.ui.cs.advprog.bidmartcatalog;

import id.ac.ui.cs.advprog.bidmartcatalog.controller.ListingInternalController;
import id.ac.ui.cs.advprog.bidmartcatalog.dto.ListingDTO;
import id.ac.ui.cs.advprog.bidmartcatalog.model.ListingStatus;
import id.ac.ui.cs.advprog.bidmartcatalog.service.ListingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListingInternalControllerTest {

    @Mock
    private ListingService listingService;

    @InjectMocks
    private ListingInternalController controller;

    @Test
    void getListingForIntegration_returnsListingDto() {
        UUID listingId = UUID.randomUUID();
        ListingDTO dto = ListingDTO.builder()
                .id(listingId)
                .title("Phone")
                .status(ListingStatus.ACTIVE)
                .build();

        when(listingService.getListingById(listingId)).thenReturn(dto);

        ResponseEntity<ListingDTO> response = controller.getListingForIntegration(listingId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Phone", response.getBody().getTitle());
        assertEquals(ListingStatus.ACTIVE, response.getBody().getStatus());
    }
}
