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

public class sonnet35_run01_NotyPevarTest {

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
        org.restscs.imp.NotyPevar instance = new org.restscs.imp.NotyPevar();
        assertNotNull(instance);
    }

    @Test
    public void testSubjectWithResultZero() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/notypevar/1/hello")
            .then()
            .statusCode(200)
            .body(equalTo("0"));
    }

    @Test
    public void testSubjectWithResultOne() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/notypevar/7/hello")
            .then()
            .statusCode(200)
            .body(equalTo("1"));
    }

    @Test
    public void testSubjectWithResultTwo() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/notypevar/1/zello")
            .then()
            .statusCode(200)
            .body(equalTo("2"));
    }

    @Test
    public void testSubjectWithResultThree() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/notypevar/6/hello")
            .then()
            .statusCode(200)
            .body(equalTo("3"));
    }

    @Test
    public void testSubjectWithResultZeroAndLargeInput() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/notypevar/1000000/hello")
            .then()
            .statusCode(200)
            .body(equalTo("0"));
    }

    @Test
    public void testSubjectWithNegativeInput() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/notypevar/-1/hello")
            .then()
            .statusCode(200)
            .body(equalTo("0"));
    }

    @Test
    public void testSubjectWithEmptyString() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/notypevar/1/")
            .then()
            .statusCode(200)
            .body(equalTo("2"));
    }

    @Test
    public void testSubjectWithSpecialCharacters() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/notypevar/1/!@#$%^&*()")
            .then()
            .statusCode(200)
            .body(equalTo("0"));
    }

    @Test
    public void testSubjectWithMaxIntegerValue() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/notypevar/2147483647/hello")
            .then()
            .statusCode(200)
            .body(equalTo("3"));
    }

    @Test
    public void testSubjectWithMinIntegerValue() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/notypevar/-2147483648/hello")
            .then()
            .statusCode(200)
            .body(equalTo("0"));
    }
}