
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

public class gpt4o_run01_RemainderTest {

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
    public void testConstructor() {
        // Ensure the constructor is executed
        new org.restncs.imp.Remainder();
    }

    @Test
    public void testRemainder_aIsZero() {
        ValidatableResponse response = given()
                .baseUri(baseUrlOfSut)
                .when()
                .get("/api/remainder/0/5")
                .then()
                .statusCode(200);
        response.body("resultAsInt", is(-1));
    }

    @Test
    public void testRemainder_bIsZero() {
        ValidatableResponse response = given()
                .baseUri(baseUrlOfSut)
                .when()
                .get("/api/remainder/5/0")
                .then()
                .statusCode(200);
        response.body("resultAsInt", is(1));
    }

    @Test
    public void testRemainder_aAndBArePositive() {
        ValidatableResponse response = given()
                .baseUri(baseUrlOfSut)
                .when()
                .get("/api/remainder/10/3")
                .then()
                .statusCode(200);
        response.body("resultAsInt", is(1));
    }

    @Test
    public void testRemainder_aPositiveBNegative() {
        ValidatableResponse response = given()
                .baseUri(baseUrlOfSut)
                .when()
                .get("/api/remainder/10/-3")
                .then()
                .statusCode(200);
        response.body("resultAsInt", is(1));
    }

    @Test
    public void testRemainder_aNegativeBPositive() {
        ValidatableResponse response = given()
                .baseUri(baseUrlOfSut)
                .when()
                .get("/api/remainder/-10/3")
                .then()
                .statusCode(200);
        response.body("resultAsInt", is(-1));
    }

    @Test
    public void testRemainder_aAndBNegative() {
        ValidatableResponse response = given()
                .baseUri(baseUrlOfSut)
                .when()
                .get("/api/remainder/-10/-3")
                .then()
                .statusCode(200);
        response.body("resultAsInt", is(-1));
    }

    @Test
    public void testRemainder_bGreaterThanA() {
        ValidatableResponse response = given()
                .baseUri(baseUrlOfSut)
                .when()
                .get("/api/remainder/3/10")
                .then()
                .statusCode(200);
        response.body("resultAsInt", is(-1));
    }

    @Test
    public void testRemainder_aAndBAreEqual() {
        ValidatableResponse response = given()
                .baseUri(baseUrlOfSut)
                .when()
                .get("/api/remainder/5/5")
                .then()
                .statusCode(200);
        response.body("resultAsInt", is(0));
    }

    @Test
    public void testRemainder_largeValues() {
        ValidatableResponse response = given()
                .baseUri(baseUrlOfSut)
                .when()
                .get("/api/remainder/1000000/999999")
                .then()
                .statusCode(400);
    }
}
