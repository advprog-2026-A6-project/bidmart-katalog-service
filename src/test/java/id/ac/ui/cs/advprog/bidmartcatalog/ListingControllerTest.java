package id.ac.ui.cs.advprog.bidmartcatalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.bidmartcatalog.controller.ListingController;
import id.ac.ui.cs.advprog.bidmartcatalog.dto.ListingDTO;
import id.ac.ui.cs.advprog.bidmartcatalog.model.Listing;
import id.ac.ui.cs.advprog.bidmartcatalog.service.ListingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ListingController.class)
@AutoConfigureMockMvc(addFilters = false)
class ListingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
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

//    @Test
//    void testCreate_ShouldReturn200AndListing() throws Exception {
//        when(listingService.createListing(any(Listing.class))).thenReturn(sampleListing);
//
//        mockMvc.perform(post("/listings")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(sampleListing)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("MacBook Pro"))
//                .andExpect(jsonPath("$.id").value(sampleId.toString()));
//    }

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
