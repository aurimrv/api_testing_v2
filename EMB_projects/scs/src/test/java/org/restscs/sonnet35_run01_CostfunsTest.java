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

public class sonnet35_run01_CostfunsTest {

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
        org.restscs.imp.Costfuns costfuns = new org.restscs.imp.Costfuns();
        assertNotNull(costfuns);
    }

    @Test
    public void testSubjectWithI5() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/costfuns/5/anyString")
            .then()
            .statusCode(200)
            .body(equalTo("10"));
    }

    @Test
    public void testSubjectWithILessThanNeg444() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/costfuns/-445/anyString")
            .then()
            .statusCode(200)
            .body(equalTo("10"));
    }

    @Test
    public void testSubjectWithILessThanOrEqualToNeg333() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/costfuns/-333/anyString")
            .then()
            .statusCode(200)
            .body(equalTo("10"));
    }

    @Test
    public void testSubjectWithIGreaterThan666() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/costfuns/667/anyString")
            .then()
            .statusCode(200)
            .body(equalTo("10"));
    }

    @Test
    public void testSubjectWithIGreaterThanOrEqualTo555() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/costfuns/555/anyString")
            .then()
            .statusCode(200)
            .body(equalTo("10"));
    }

    @Test
    public void testSubjectWithINotEqualToNeg4() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/costfuns/0/anyString")
            .then()
            .statusCode(200)
            .body(equalTo("10"));
    }

    @Test
    public void testSubjectWithSEqualToS1PlusS2() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/costfuns/0/baab")
            .then()
            .statusCode(200)
            .body(equalTo("10"));
    }

    @Test
    public void testSubjectWithSGreaterThanS2PlusS2PlusS1() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/costfuns/0/ababba")
            .then()
            .statusCode(200)
            .body(equalTo("10"));
    }

    @Test
    public void testSubjectWithSGreaterThanOrEqualToS2PlusS2PlusS1() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/costfuns/0/ababba")
            .then()
            .statusCode(200)
            .body(equalTo("10"));
    }

    @Test
    public void testSubjectWithSNotEqualToS2PlusS2() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/costfuns/0/abc")
            .then()
            .statusCode(200)
            .body(equalTo("10"));
    }

    @Test
    public void testSubjectWithDefaultCase() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/costfuns/-4/abab")
            .then()
            .statusCode(200)
            .body(equalTo("10"));
    }
}
