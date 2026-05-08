package id.ac.ui.cs.advprog.bidmartcatalog;

import com.fasterxml.jackson.databind.ObjectMapper;

import id.ac.ui.cs.advprog.bidmartcatalog.controller.ListingController;
import id.ac.ui.cs.advprog.bidmartcatalog.service.ListingService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ListingController.class)
@AutoConfigureMockMvc(addFilters = false)
class ListingControllerTestTwo {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ListingService listingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void cancelListing_shouldReturnOk() throws Exception {

        UUID listingId = UUID.randomUUID();

        doNothing().when(listingService)
                .cancelListing(listingId);

        mockMvc.perform(
                post("/listings/" + listingId + "/cancel")
        ).andExpect(status().isOk());

        verify(listingService).cancelListing(listingId);
    }

    @Test
    void getAll_shouldReturnList() throws Exception {

        when(listingService.getAllListings())
                .thenReturn(List.of());

        mockMvc.perform(get("/listings"))
                .andExpect(status().isOk());
    }

    @Test
    void search_shouldReturnOk() throws Exception {

        when(listingService.searchListings(
                any(),
                any(),
                any(),
                any(),
                any()
        )).thenReturn(List.of());

        mockMvc.perform(
                get("/listings/search")
                        .param("keyword", "phone")
        ).andExpect(status().isOk());
    }
}