package id.ac.ui.cs.advprog.bidmartcatalog.functional;

import id.ac.ui.cs.advprog.bidmartcatalog.dto.ListingBidStatusResponse;
import id.ac.ui.cs.advprog.bidmartcatalog.dto.SellerPublicProfileDTO;
import id.ac.ui.cs.advprog.bidmartcatalog.functional.config.FunctionalTestInfrastructureConfig;
import id.ac.ui.cs.advprog.bidmartcatalog.functional.support.CatalogApiSteps;
import id.ac.ui.cs.advprog.bidmartcatalog.model.ListingStatus;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Feature;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.annotations.Story;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

@ExtendWith(SerenityJUnit5Extension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("serenity")
@Import(FunctionalTestInfrastructureConfig.class)
@Tag("functional")
@Feature("Catalog and Marketplace Listings")
class CatalogSerenityFunctionalTest {

    private static final String SELLER_ID = "serenity-seller-1001";

    @LocalServerPort
    private int port;

    @MockitoBean
    private RestTemplate restTemplate;

    @Steps
    private CatalogApiSteps catalogApi;

    @BeforeEach
    void stubExternalServices() {
        lenient().when(restTemplate.getForObject(anyString(), eq(ListingBidStatusResponse.class)))
                .thenAnswer(invocation -> {
                    String url = invocation.getArgument(0);
                    int bidsIndex = url.indexOf("/bids/status");
                    String pathBeforeBids = url.substring(0, bidsIndex);
                    UUID listingId = UUID.fromString(pathBeforeBids.substring(pathBeforeBids.lastIndexOf('/') + 1));
                    return new ListingBidStatusResponse(listingId, false, 0L);
                });

        lenient().when(restTemplate.exchange(
                        anyString(),
                        eq(HttpMethod.GET),
                        any(),
                        eq(SellerPublicProfileDTO.class)))
                .thenReturn(ResponseEntity.ok(
                        SellerPublicProfileDTO.builder()
                                .id(1001L)
                                .name("Serenity Seller")
                                .bio("Functional test seller profile")
                                .profilePictureUrl("https://example.com/seller.jpg")
                                .build()));
    }

    @Test
    @Story("Seller creates a listing and manages their catalog")
    @DisplayName("seller can create a listing and retrieve it from GET /listings/mine")
    void sellerCanCreateListingAndSeeItInMine() {
        UUID categoryId = firstLeafCategoryId();
        String title = "Serenity Vintage Camera " + UUID.randomUUID();

        Response created = catalogApi.createListing(port, SELLER_ID, categoryId, title);
        UUID listingId = UUID.fromString(created.jsonPath().getString("id"));

        Response mine = catalogApi.getMyListings(port, SELLER_ID);
        assertThat(mine.jsonPath().getList("id")).contains(listingId.toString());

        Response detail = catalogApi.getListingById(port, listingId);
        assertThat(detail.jsonPath().getString("title")).isEqualTo(title);
        assertThat(detail.jsonPath().getString("sellerId")).isEqualTo(SELLER_ID);

        Serenity.recordReportData()
                .withTitle("Seller listing created")
                .andContents("Seller " + SELLER_ID + " created listing " + listingId + " visible in /listings/mine.");
    }

    @Test
    @Story("Marketplace browse and search")
    @DisplayName("buyers can browse all listings and search by keyword")
    void buyersCanBrowseAndSearchListings() {
        UUID categoryId = firstLeafCategoryId();
        String title = "Serenity Searchable Guitar " + UUID.randomUUID();

        catalogApi.createListing(port, SELLER_ID, categoryId, title);
        catalogApi.getCategoryTree(port);

        Response allListings = catalogApi.getAllListings(port);
        assertThat(allListings.jsonPath().getList("$")).isNotEmpty();

        Response searchResults = catalogApi.searchListings(port, "Searchable Guitar");
        assertThat(searchResults.jsonPath().getList("title"))
                .anyMatch(foundTitle -> foundTitle.toString().contains("Searchable Guitar"));

        Serenity.recordReportData()
                .withTitle("Catalog browse and search")
                .andContents("Public listing index and keyword search returned the newly created listing.");
    }

    @Test
    @Story("Seller updates listing details when no bids exist")
    @DisplayName("seller can update description and image when auction service reports no bids")
    void sellerCanUpdateListingWithoutBids() {
        UUID categoryId = firstLeafCategoryId();
        Response created = catalogApi.createListing(
                port,
                SELLER_ID,
                categoryId,
                "Serenity Updatable Watch " + UUID.randomUUID()
        );
        UUID listingId = UUID.fromString(created.jsonPath().getString("id"));

        Response updated = catalogApi.updateListing(
                port,
                listingId,
                SELLER_ID,
                "Updated by Serenity functional test"
        );

        assertThat(updated.jsonPath().getString("description"))
                .isEqualTo("Updated by Serenity functional test");
        assertThat(updated.jsonPath().getString("imageUrl"))
                .isEqualTo("https://example.com/serenity-updated.jpg");

        Serenity.recordReportData()
                .withTitle("Listing update without bids")
                .andContents("Listing " + listingId + " was updated while bid status remained empty.");
    }

    @Test
    @Story("Seller cancels listing when no bids exist")
    @DisplayName("seller can cancel an active listing when auction service reports no bids")
    void sellerCanCancelListingWithoutBids() {
        UUID categoryId = firstLeafCategoryId();
        Response created = catalogApi.createListing(
                port,
                SELLER_ID,
                categoryId,
                "Serenity Cancellable Lamp " + UUID.randomUUID()
        );
        UUID listingId = UUID.fromString(created.jsonPath().getString("id"));

        catalogApi.cancelListing(port, listingId, SELLER_ID);

        Response detail = catalogApi.getListingById(port, listingId);
        assertThat(detail.jsonPath().getString("status")).isEqualTo(ListingStatus.CANCELLED.name());

        Serenity.recordReportData()
                .withTitle("Listing cancellation")
                .andContents("Listing " + listingId + " moved to CANCELLED after seller cancel request.");
    }

    private UUID firstLeafCategoryId() {
        Response tree = catalogApi.getCategoryTree(port);
        String categoryId = tree.jsonPath().getString("[0].children[0].id");
        return UUID.fromString(categoryId);
    }
}
