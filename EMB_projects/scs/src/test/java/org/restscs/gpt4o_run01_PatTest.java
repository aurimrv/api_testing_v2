
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

public class gpt4o_run01_PatTest {

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
    public void testReverseMethodEmptyString() {
        // Test the Reverse method with an empty string
        String input = "";
        String response = given()
                .baseUri(baseUrlOfSut)
                .when()
                .get("/api/pat/" + input)
                .then()
                .statusCode(200)
                .extract()
                .asString();
        assertEquals("", response);
    }

    @Test
    public void testReverseMethodSingleCharacter() {
        // Test the Reverse method with a single character
        String input = "a";
        String response = given()
                .baseUri(baseUrlOfSut)
                .when()
                .get("/api/pat/" + input)
                .then()
                .statusCode(200)
                .extract()
                .asString();
        assertEquals("none", response);
    }

    @Test
    public void testReverseMethodMultipleCharacters() {
        // Test the Reverse method with a string of multiple characters
        String input = "abc";
        String expected = "none";
        String response = given()
                .baseUri(baseUrlOfSut)
                .when()
                .get("/api/pat/" + input)
                .then()
                .statusCode(200)
                .extract()
                .asString();
        assertEquals(expected, response);
    }

    @Test
    public void testSubjectMethodExactMatch() {
        // Test the subject method with an exact match (txt contains pat)
        String txt = "hello";
        String pat = "ell";
        String response = given()
                .baseUri(baseUrlOfSut)
                .when()
                .get("/api/pat/" + txt + "/" + pat)
                .then()
                .statusCode(200)
                .extract()
                .asString();
        assertEquals("1", response);
    }

    @Test
    public void testSubjectMethodReverseMatch() {
        // Test the subject method with a reverse match (txt contains reverse of pat)
        String txt = "hello";
        String pat = "lle";
        String response = given()
                .baseUri(baseUrlOfSut)
                .when()
                .get("/api/pat/" + txt + "/" + pat)
                .then()
                .statusCode(404)
                .extract()
                .asString();
        assertEquals("2", response);
    }

    @Test
    public void testSubjectMethodBothMatch() {
        // Test the subject method with both pat and reverse of pat present
        String txt = "hellolleh";
        String pat = "ell";
        String response = given()
                .baseUri(baseUrlOfSut)
                .when()
                .get("/api/pat/" + txt + "/" + pat)
                .then()
                .statusCode(200)
                .extract()
                .asString();
        assertEquals("1", response);
    }

    @Test
    public void testSubjectMethodPalindromeForward() {
        // Test the subject method for palindrome (pat + reverse of pat)
        String txt = "elle";
        String pat = "ell";
        String response = given()
                .baseUri(baseUrlOfSut)
                .when()
                .get("/api/pat/" + txt + "/" + pat)
                .then()
                .statusCode(200)
                .extract()
                .asString();
        assertEquals("2", response);
    }

    @Test
    public void testSubjectMethodPalindromeBackward() {
        // Test the subject method for palindrome (reverse of pat + pat)
        String txt = "lleell";
        String pat = "ell";
        String response = given()
                .baseUri(baseUrlOfSut)
                .when()
                .get("/api/pat/" + txt + "/" + pat)
                .then()
                .statusCode(200)
                .extract()
                .asString();
        assertEquals("0", response);
    }

    @Test
    public void testSubjectMethodNoMatch() {
        // Test the subject method with no match
        String txt = "hello";
        String pat = "xyz";
        String response = given()
                .baseUri(baseUrlOfSut)
                .when()
                .get("/api/pat/" + txt + "/" + pat)
                .then()
                .statusCode(200)
                .extract()
                .asString();
        assertEquals("0", response);
    }

    @Test
    public void testConstructorExecution() {
        // Explicitly invoke the constructor to test its execution
        org.restscs.imp.Pat instance = new org.restscs.imp.Pat();
        assertNotNull(instance);
    }
}
