package id.ac.ui.cs.advprog.bidmartcatalog.functional.support;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static net.serenitybdd.rest.SerenityRest.given;

public class CatalogApiSteps {

    @Step("GET /api/categories/tree")
    public Response getCategoryTree(int port) {
        return given()
                .baseUri("http://localhost")
                .port(port)
                .when()
                .get("/api/categories/tree")
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    @Step("POST /listings for seller {2}")
    public Response createListing(int port, String sellerId, UUID categoryId, String title) {
        return given()
                .baseUri("http://localhost")
                .port(port)
                .header("X-User-Id", sellerId)
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "title", title,
                        "description", "Serenity functional listing description",
                        "startingPrice", new BigDecimal("100000"),
                        "reservePrice", new BigDecimal("150000"),
                        "currentPrice", new BigDecimal("100000"),
                        "imageUrl", "https://example.com/serenity-listing.jpg",
                        "categoryId", categoryId.toString(),
                        "startTime", LocalDateTime.now().plusHours(1).toString(),
                        "endTime", LocalDateTime.now().plusDays(7).toString()
                ))
                .when()
                .post("/listings")
                .then()
                .statusCode(201)
                .extract()
                .response();
    }

    @Step("GET /listings/mine for seller {1}")
    public Response getMyListings(int port, String sellerId) {
        return given()
                .baseUri("http://localhost")
                .port(port)
                .header("X-User-Id", sellerId)
                .when()
                .get("/listings/mine")
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    @Step("GET /listings")
    public Response getAllListings(int port) {
        return given()
                .baseUri("http://localhost")
                .port(port)
                .when()
                .get("/listings")
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    @Step("GET /listings/{id}")
    public Response getListingById(int port, UUID listingId) {
        return given()
                .baseUri("http://localhost")
                .port(port)
                .when()
                .get("/listings/{id}", listingId)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    @Step("PUT /listings/{id} for seller {2}")
    public Response updateListing(int port, UUID listingId, String sellerId, String description) {
        return given()
                .baseUri("http://localhost")
                .port(port)
                .header("X-User-Id", sellerId)
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "description", description,
                        "imageUrl", "https://example.com/serenity-updated.jpg"
                ))
                .when()
                .put("/listings/{id}", listingId)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    @Step("POST /listings/{id}/cancel for seller {2}")
    public void cancelListing(int port, UUID listingId, String sellerId) {
        given()
                .baseUri("http://localhost")
                .port(port)
                .header("X-User-Id", sellerId)
                .when()
                .post("/listings/{listingId}/cancel", listingId)
                .then()
                .statusCode(200);
    }

    @Step("GET /listings/search?keyword={1}")
    public Response searchListings(int port, String keyword) {
        return given()
                .baseUri("http://localhost")
                .port(port)
                .queryParam("keyword", keyword)
                .when()
                .get("/listings/search")
                .then()
                .statusCode(200)
                .extract()
                .response();
    }
}
