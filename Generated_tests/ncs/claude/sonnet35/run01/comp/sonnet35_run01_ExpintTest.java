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

public class sonnet35_run01_ExpintTest {

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
    public void testExpintConstructor() {
        org.restncs.imp.Expint expint = new org.restncs.imp.Expint();
        assertNotNull(expint);
    }

    @Test
    public void testExpintNegativeNOrX() {
        given()
            .accept("application/json")
        .when()
            .get(baseUrlOfSut + "/api/expint/-1/1.0")
        .then()
            .statusCode(500)
            .body("message", containsString("error: n < 0 or x < 0"));

        given()
            .accept("application/json")
        .when()
            .get(baseUrlOfSut + "/api/expint/1/-1.0")
        .then()
            .statusCode(500)
            .body("message", containsString("error: n < 0 or x < 0"));
    }

    @Test
    public void testExpintZeroXWithNZeroOrOne() {
        given()
            .accept("application/json")
        .when()
            .get(baseUrlOfSut + "/api/expint/0/0.0")
        .then()
            .statusCode(500)
            .body("message", containsString("error: n < 0 or x < 0"));

        given()
            .accept("application/json")
        .when()
            .get(baseUrlOfSut + "/api/expint/1/0.0")
        .then()
            .statusCode(500)
            .body("message", containsString("error: n < 0 or x < 0"));
    }

    @Test
    public void testExpintNZero() {
        given()
            .accept("application/json")
        .when()
            .get(baseUrlOfSut + "/api/expint/0/1.0")
        .then()
            .statusCode(200)
            .body("resultAsDouble", closeTo(0.3678794, 1e-7));
    }

    @Test
    public void testExpintXZero() {
        given()
            .accept("application/json")
        .when()
            .get(baseUrlOfSut + "/api/expint/2/0.0")
        .then()
            .statusCode(200)
            .body("resultAsDouble", closeTo(1.0, 1e-7));
    }

    @Test
    public void testExpintXGreaterThanOne() {
        given()
            .accept("application/json")
        .when()
            .get(baseUrlOfSut + "/api/expint/2/2.0")
        .then()
            .statusCode(200)
            .body("resultAsDouble", closeTo(0.0489005, 1e-7));
    }

    @Test
    public void testExpintXLessThanOrEqualToOne() {
        given()
            .accept("application/json")
        .when()
            .get(baseUrlOfSut + "/api/expint/2/0.5")
        .then()
            .statusCode(200)
            .body("resultAsDouble", closeTo(0.5597736, 1e-7));
    }

    @Test
    public void testExpintContinuedFractionFailure() {
        given()
            .accept("application/json")
        .when()
            .get(baseUrlOfSut + "/api/expint/1000000/1000000.0")
        .then()
            .statusCode(500)
            .body("message", containsString("continued fraction failed in expint"));
    }

    @Test
    public void testExpintSeriesFailure() {
        given()
            .accept("application/json")
        .when()
            .get(baseUrlOfSut + "/api/expint/1000000/0.1")
        .then()
            .statusCode(500)
            .body("message", containsString("series failed in expint"));
    }
}