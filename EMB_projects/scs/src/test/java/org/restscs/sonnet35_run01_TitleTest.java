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

public class sonnet35_run01_TitleTest {

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
    public void testMaleTitles() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/male/mr")
            .then()
            .statusCode(200)
            .body(equalTo("1"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/male/dr")
            .then()
            .statusCode(200)
            .body(equalTo("1"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/male/sir")
            .then()
            .statusCode(200)
            .body(equalTo("1"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/male/rev")
            .then()
            .statusCode(200)
            .body(equalTo("1"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/male/rthon")
            .then()
            .statusCode(200)
            .body(equalTo("1"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/male/prof")
            .then()
            .statusCode(200)
            .body(equalTo("1"));
    }

    @Test
    public void testFemaleTitles() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/female/mrs")
            .then()
            .statusCode(200)
            .body(equalTo("0"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/female/miss")
            .then()
            .statusCode(200)
            .body(equalTo("0"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/female/ms")
            .then()
            .statusCode(200)
            .body(equalTo("0"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/female/dr")
            .then()
            .statusCode(200)
            .body(equalTo("0"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/female/lady")
            .then()
            .statusCode(200)
            .body(equalTo("0"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/female/rev")
            .then()
            .statusCode(200)
            .body(equalTo("0"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/female/rthon")
            .then()
            .statusCode(200)
            .body(equalTo("0"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/female/prof")
            .then()
            .statusCode(200)
            .body(equalTo("0"));
    }

    @Test
    public void testNoneTitles() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/none/dr")
            .then()
            .statusCode(200)
            .body(equalTo("2"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/none/rev")
            .then()
            .statusCode(200)
            .body(equalTo("2"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/none/rthon")
            .then()
            .statusCode(200)
            .body(equalTo("2"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/none/prof")
            .then()
            .statusCode(200)
            .body(equalTo("2"));
    }

    @Test
    public void testInvalidTitles() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/male/mrs")
            .then()
            .statusCode(200)
            .body(equalTo("-1"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/female/mr")
            .then()
            .statusCode(200)
            .body(equalTo("-1"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/none/mr")
            .then()
            .statusCode(200)
            .body(equalTo("-1"));
    }

    @Test
    public void testCaseInsensitivity() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/MALE/MR")
            .then()
            .statusCode(200)
            .body(equalTo("1"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/Female/Ms")
            .then()
            .statusCode(200)
            .body(equalTo("0"));

        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/NONE/DR")
            .then()
            .statusCode(200)
            .body(equalTo("2"));
    }

    @Test
    public void testInvalidSex() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/title/invalid/mr")
            .then()
            .statusCode(200)
            .body(equalTo("-1"));
    }

    @Test
    public void testConstructor() {
        org.restscs.imp.Title title = new org.restscs.imp.Title();
        assertNotNull(title);
    }
}