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

public class sonnet35_run01_CORSFilterTest {

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
    public void testCORSFilterConstructor() {
        // Test the constructor (init method)
        eu.fayder.restcountries.servlet.CORSFilter corsFilter = new eu.fayder.restcountries.servlet.CORSFilter();
        assertNotNull(corsFilter);
    }

    @Test
    public void testCORSFilterDoFilter() {
        // Test the doFilter method by making a request and checking the CORS headers
        given()
            .baseUri(baseUrlOfSut)
        .when()
            .get("/v1/all")
        .then()
            .statusCode(200)
            .header("Access-Control-Allow-Origin", equalTo("*"))
            .header("Access-Control-Allow-Methods", equalTo("GET"))
            .header("Access-Control-Allow-Headers", equalTo("Accept, X-Requested-With"))
            .header("Cache-Control", equalTo("public, max-age=86400"));
    }

    @Test
    public void testCORSFilterDestroy() {
        // The destroy method is empty, but we can still test it for coverage
        eu.fayder.restcountries.servlet.CORSFilter corsFilter = new eu.fayder.restcountries.servlet.CORSFilter();
        corsFilter.destroy();
        // No assertion needed as the method is empty
    }
}