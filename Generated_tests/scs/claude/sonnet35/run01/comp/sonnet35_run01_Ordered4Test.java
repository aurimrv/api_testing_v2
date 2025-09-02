package org.restscs;

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

public class sonnet35_run01_Ordered4Test {

    private static final SutHandler controller = new em.embedded.org.restscs.EmbeddedEvoMasterController();
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
    public void testConstructor() {
        org.restscs.imp.Ordered4 ordered4 = new org.restscs.imp.Ordered4();
        assertNotNull(ordered4);
    }

    @Test
    public void testUnordered() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/ordered4/{w}/{x}/{z}/{y}", "abcde", "abcde", "abcde", "abcde")
            .then()
            .statusCode(200)
            .body(equalTo("unordered"));
    }

    @Test
    public void testIncreasing() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/ordered4/{w}/{x}/{z}/{y}", "abcde", "bcdef", "defgh", "cdefg")
            .then()
            .statusCode(200)
            .body(equalTo("increasing"));
    }

    @Test
    public void testDecreasing() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/ordered4/{w}/{x}/{z}/{y}", "defgh", "cdefg", "abcde", "bcdef")
            .then()
            .statusCode(200)
            .body(equalTo("decreasing"));
    }

    @Test
    public void testInvalidLength() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/ordered4/{w}/{x}/{z}/{y}", "abcd", "bcdef", "defgh", "cdefg")
            .then()
            .statusCode(200)
            .body(equalTo("unordered"));
    }

    @Test
    public void testBoundaryLength() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/ordered4/{w}/{x}/{z}/{y}", "abcde", "bcdef", "cdefgh", "defgh")
            .then()
            .statusCode(200)
            .body(equalTo("increasing"));
    }

    @Test
    public void testEqualStrings() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/ordered4/{w}/{x}/{z}/{y}", "abcde", "abcde", "abcde", "abcde")
            .then()
            .statusCode(200)
            .body(equalTo("unordered"));
    }
}