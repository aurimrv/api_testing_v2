package org.restncs;

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

public class sonnet35_run01_FisherTest {

    private static final SutHandler controller = new em.embedded.org.restncs.EmbeddedEvoMasterController();
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
    public void testFisherConstructor() {
        org.restncs.imp.Fisher fisher = new org.restncs.imp.Fisher();
        assertNotNull(fisher);
    }

    @Test
    public void testFisherExeA1B1() {
        double result = given()
            .accept("application/json")
            .get(baseUrlOfSut + "/api/fisher/1/1/0.5")
            .then()
            .statusCode(200)
            .extract()
            .path("resultAsDouble");

        assertEquals(1.0471975511965976, result, 1e-10);
    }

    @Test
    public void testFisherExeA1BNot1() {
        double result = given()
            .accept("application/json")
            .get(baseUrlOfSut + "/api/fisher/1/2/0.5")
            .then()
            .statusCode(200)
            .extract()
            .path("resultAsDouble");

        assertEquals(0.5773502691896257, result, 1e-10);
    }

    @Test
    public void testFisherExeANot1B1() {
        double result = given()
            .accept("application/json")
            .get(baseUrlOfSut + "/api/fisher/2/1/0.5")
            .then()
            .statusCode(200)
            .extract()
            .path("resultAsDouble");

        assertEquals(0.5857864376269049, result, 1e-10);
    }

    @Test
    public void testFisherExeANot1BNot1() {
        double result = given()
            .accept("application/json")
            .get(baseUrlOfSut + "/api/fisher/2/2/0.5")
            .then()
            .statusCode(200)
            .extract()
            .path("resultAsDouble");

        assertEquals(0.5, result, 1e-10);
    }

    @Test
    public void testFisherExeA1LargeN() {
        double result = given()
            .accept("application/json")
            .get(baseUrlOfSut + "/api/fisher/1/10/0.5")
            .then()
            .statusCode(200)
            .extract()
            .path("resultAsDouble");

        assertTrue(result > 0 && result < 1);
    }

    @Test
    public void testFisherExeLargeMLargeN() {
        double result = given()
            .accept("application/json")
            .get(baseUrlOfSut + "/api/fisher/10/10/0.5")
            .then()
            .statusCode(200)
            .extract()
            .path("resultAsDouble");

        assertTrue(result > 0 && result < 1);
    }

    @Test
    public void testFisherExeNegativeResult() {
        double result = given()
            .accept("application/json")
            .get(baseUrlOfSut + "/api/fisher/1/1/0.0001")
            .then()
            .statusCode(200)
            .extract()
            .path("resultAsDouble");

        assertEquals(0.0, result, 1e-10);
    }

    @Test
    public void testFisherExeResultGreaterThanOne() {
        double result = given()
            .accept("application/json")
            .get(baseUrlOfSut + "/api/fisher/1/1/1000")
            .then()
            .statusCode(200)
            .extract()
            .path("resultAsDouble");

        assertEquals(1.0, result, 1e-10);
    }
}
