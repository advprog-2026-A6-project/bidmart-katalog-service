package id.ac.ui.cs.advprog.bidmartcatalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.bidmartcatalog.controller.ListingController;
import id.ac.ui.cs.advprog.bidmartcatalog.dto.CreateListingRequest;
import id.ac.ui.cs.advprog.bidmartcatalog.dto.ListingDTO;
import id.ac.ui.cs.advprog.bidmartcatalog.dto.UpdateListingRequest;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Listing;
import id.ac.ui.cs.advprog.bidmartcatalog.service.ListingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ListingController.class)
@AutoConfigureMockMvc(addFilters = false)
class ListingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ListingService listingService;

    @Autowired
    private ObjectMapper objectMapper;

    private Listing sampleListing;
    private ListingDTO sampleListingDto;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        sampleListing = new Listing();
        sampleListing.setId(sampleId);
        sampleListing.setTitle("MacBook Pro");

        sampleListingDto = ListingDTO.builder()
                .id(sampleId)
                .title("MacBook Pro")
                .sellerId("1")
                .sellerName("Seller One")
                .build();
    }

    @Test
    void testCreate_ShouldReturn201AndListing() throws Exception {
        CreateListingRequest request = new CreateListingRequest();
        request.setTitle("MacBook Pro");
        request.setDescription("M3 chip");
        request.setStartingPrice(new BigDecimal("10000000"));
        request.setCategoryId(UUID.randomUUID());
        request.setStartTime(LocalDateTime.now());
        request.setEndTime(LocalDateTime.now().plusDays(7));

        when(listingService.createListing(any(CreateListingRequest.class), eq("seller-42")))
                .thenReturn(sampleListing);

        mockMvc.perform(post("/listings")
                        .header("X-User-Id", "seller-42")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("MacBook Pro"));
    }

    @Test
    void testUpdateListing_ShouldReturnUpdatedDto() throws Exception {
        UpdateListingRequest request = new UpdateListingRequest();
        request.setDescription("Updated");
        request.setImageUrl("new.jpg");

        when(listingService.updateListing(any(UpdateListingRequest.class), eq(sampleId)))
                .thenReturn(sampleListingDto);

        mockMvc.perform(put("/listings/{id}", sampleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("MacBook Pro"));
    }

    @Test
    void testGetById_ShouldReturnListing() throws Exception {
        when(listingService.getListingById(sampleId)).thenReturn(sampleListingDto);

        mockMvc.perform(get("/listings/{id}", sampleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("MacBook Pro"))
                .andExpect(jsonPath("$.sellerName").value("Seller One"));
    }

    @Test
    void testDelete_ShouldReturn200() throws Exception {
        mockMvc.perform(delete("/listings/{id}", sampleId))
                .andExpect(status().isOk());
    }
}
