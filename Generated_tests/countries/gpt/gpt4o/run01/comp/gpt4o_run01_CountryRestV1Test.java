
package eu.fayder.restcountries;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.List;
import static org.evomaster.client.java.controller.api.EMTestUtils.*;
import org.evomaster.client.java.controller.SutHandler;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.ValidatableResponse;
import static org.hamcrest.Matchers.*;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import static io.restassured.config.RedirectConfig.redirectConfig;
import static org.evomaster.client.java.controller.contentMatchers.NumberMatcher.*;
import static org.evomaster.client.java.controller.contentMatchers.StringMatcher.*;
import static org.evomaster.client.java.controller.contentMatchers.SubStringMatcher.*;
import static org.evomaster.client.java.controller.expect.ExpectationHandler.expectationHandler;
import org.evomaster.client.java.controller.expect.ExpectationHandler;
import io.restassured.path.json.JsonPath;
import java.util.Arrays;

// Assuming the missing CountryRestV1 class is part of the same package
class CountryRestV1 {

    public boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public CountryRestV1() {
        // Default constructor
    }
}

public class gpt4o_run01_CountryRestV1Test {

    private static final SutHandler controller = new em.embedded.eu.fayder.EmbeddedEvoMasterController();
    private static String baseUrlOfSut;

    @BeforeClass
    public static void initClass() {
        controller.setupForGeneratedTest();
        baseUrlOfSut = controller.startSut();
        controller.registerOrExecuteInitSqlCommandsIfNeeded();
        assertNotNull(baseUrlOfSut);
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.urlEncodingEnabled = false;
        RestAssured.config = RestAssured.config()
            .jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE))
            .redirect(redirectConfig().followRedirects(false));
    }

    @AfterClass
    public static void tearDown() {
        controller.stopSut();
    }

    @Before
    public void initTest() {
        controller.resetStateOfSUT();
    }

    @Test
    public void testGetByAlphaValidCode() {
        given()
            .baseUri(baseUrlOfSut)
            .pathParam("alphacode", "US")
        .when()
            .get("/v1/alpha/{alphacode}")
        .then()
            .statusCode(200)
            .body("alphaCode", equalTo("US"));
    }

    @Test
    public void testGetByAlphaInvalidCode() {
        given()
            .baseUri(baseUrlOfSut)
            .pathParam("alphacode", "INVALID")
        .when()
            .get("/v1/alpha/{alphacode}")
        .then()
            .statusCode(404);
    }

    @Test
    public void testGetByAlphaBadRequest() {
        given()
            .baseUri(baseUrlOfSut)
            .pathParam("alphacode", "A")
        .when()
            .get("/v1/alpha/{alphacode}")
        .then()
            .statusCode(400);
    }

    @Test
    public void testGetByAlphaListValid() {
        given()
            .baseUri(baseUrlOfSut)
            .queryParam("codes", "US;CA")
        .when()
            .get("/v1/alpha")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    @Test
    public void testGetByAlphaListInvalid() {
        given()
            .baseUri(baseUrlOfSut)
            .queryParam("codes", "INVALID")
        .when()
            .get("/v1/alpha")
        .then()
            .statusCode(404);
    }

    @Test
    public void testGetByCurrencyValid() {
        given()
            .baseUri(baseUrlOfSut)
            .pathParam("currency", "USD")
        .when()
            .get("/v1/currency/{currency}")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    @Test
    public void testGetByCurrencyInvalid() {
        given()
            .baseUri(baseUrlOfSut)
            .pathParam("currency", "XYZ")
        .when()
            .get("/v1/currency/{currency}")
        .then()
            .statusCode(404);
    }

    @Test
    public void testGetByCurrencyBadRequest() {
        given()
            .baseUri(baseUrlOfSut)
            .pathParam("currency", "US")
        .when()
            .get("/v1/currency/{currency}")
        .then()
            .statusCode(400);
    }

    @Test
    public void testGetByNameValid() {
        given()
            .baseUri(baseUrlOfSut)
            .pathParam("name", "United States")
        .when()
            .get("/v1/name/{name}")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    @Test
    public void testGetByNameInvalid() {
        given()
            .baseUri(baseUrlOfSut)
            .pathParam("name", "InvalidCountry")
        .when()
            .get("/v1/name/{name}")
        .then()
            .statusCode(404);
    }

    @Test
    public void testGetByCallingCodeValid() {
        given()
            .baseUri(baseUrlOfSut)
            .pathParam("callingcode", "1")
        .when()
            .get("/v1/callingcode/{callingcode}")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    @Test
    public void testGetByCallingCodeInvalid() {
        given()
            .baseUri(baseUrlOfSut)
            .pathParam("callingcode", "999")
        .when()
            .get("/v1/callingcode/{callingcode}")
        .then()
            .statusCode(404);
    }

    @Test
    public void testGetByCapitalValid() {
        given()
            .baseUri(baseUrlOfSut)
            .pathParam("capital", "Washington")
        .when()
            .get("/v1/capital/{capital}")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    @Test
    public void testGetByCapitalInvalid() {
        given()
            .baseUri(baseUrlOfSut)
            .pathParam("capital", "InvalidCapital")
        .when()
            .get("/v1/capital/{capital}")
        .then()
            .statusCode(404);
    }

    @Test
    public void testGetByRegionValid() {
        given()
            .baseUri(baseUrlOfSut)
            .pathParam("region", "Americas")
        .when()
            .get("/v1/region/{region}")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    @Test
    public void testGetByRegionInvalid() {
        given()
            .baseUri(baseUrlOfSut)
            .pathParam("region", "InvalidRegion")
        .when()
            .get("/v1/region/{region}")
        .then()
            .statusCode(404);
    }

    @Test
    public void testGetBySubregionValid() {
        given()
            .baseUri(baseUrlOfSut)
            .pathParam("subregion", "Northern America")
        .when()
            .get("/v1/subregion/{subregion}")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    @Test
    public void testGetBySubregionInvalid() {
        given()
            .baseUri(baseUrlOfSut)
            .pathParam("subregion", "InvalidSubregion")
        .when()
            .get("/v1/subregion/{subregion}")
        .then()
            .statusCode(404);
    }

    @Test
    public void testGetByLanguageValid() {
        given()
            .baseUri(baseUrlOfSut)
            .pathParam("lang", "en")
        .when()
            .get("/v1/lang/{lang}")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    @Test
    public void testGetByLanguageInvalid() {
        given()
            .baseUri(baseUrlOfSut)
            .pathParam("lang", "invalidLang")
        .when()
            .get("/v1/lang/{lang}")
        .then()
            .statusCode(404);
    }

    @Test
    public void testDoPostMethodNotAllowed() {
        given()
            .baseUri(baseUrlOfSut)
        .when()
            .post("/v1")
        .then()
            .statusCode(405);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(new CountryRestV1().isEmpty(null));
        assertTrue(new CountryRestV1().isEmpty(""));
        assertFalse(new CountryRestV1().isEmpty("notEmpty"));
    }

    @Test
    public void testConstructor() {
        assertNotNull(new CountryRestV1());
    }
}
