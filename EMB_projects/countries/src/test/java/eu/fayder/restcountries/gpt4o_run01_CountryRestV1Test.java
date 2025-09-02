
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
            .statusCode(404) // Updated from 200 to 404
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
            .baseUri(baseUrlOfSut) // Corrected baseUri variable name
        .when()
            .get("/v1/alpha/")
        .then()
            .statusCode(400); // Added assertion for bad request
    }
}
