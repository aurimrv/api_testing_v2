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
import org.restncs.imp.Bessj;

public class sonnet35_run01_BessjTest {

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
    public void testBessjConstructor() {
        Bessj bessj = new Bessj();
        assertNotNull(bessj);
    }

    @Test
    public void testBessjWithValidInput() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/api/bessj/3/2.5")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testBessjWithNLessThan2() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/api/bessj/1/2.5")
            .then()
            .statusCode(500);
    }

    @Test
    public void testBessjWithXEqualZero() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/api/bessj/3/0.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", equalTo(0.0f));
    }

    @Test
    public void testBessjWithXGreaterThanN() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/api/bessj/3/5.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testBessjWithXLessThanN() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/api/bessj/5/2.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testBessjWithNegativeX() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/api/bessj/3/-2.5")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testBessjWithLargeN() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/api/bessj/100/50.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testBessjWithLargeX() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/api/bessj/3/1000.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testBessj1WithSmallX() {
        Bessj bessj = new Bessj();
        double result = bessj.bessj(1, 0.1);
        assertNotNull(result);
    }

    @Test
    public void testBessj1WithLargeX() {
        Bessj bessj = new Bessj();
        double result = bessj.bessj(1, 100.0);
        assertNotNull(result);
    }
}