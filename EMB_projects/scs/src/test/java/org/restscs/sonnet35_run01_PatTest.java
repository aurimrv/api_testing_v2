package org.restscs;

import org.junit.AfterClass;
import org.junit.Ignore;
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

public class sonnet35_run01_PatTest {

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
        org.restscs.imp.Pat pat = new org.restscs.imp.Pat();
        assertNotNull(pat);
    }

    @Test
    public void testReverseShortString() {
        String result = org.restscs.imp.Pat.subject("ab", "ba");
        assertEquals("0", result);
    }

    @Test
    public void testReverseLongString() {
        String result = org.restscs.imp.Pat.subject("hello", "olleh");
        assertEquals("2", result);
    }

    @Test
    public void testSubjectPatNotFound() {
        String result = org.restscs.imp.Pat.subject("hello", "xyz");
        assertEquals("0", result);
    }

    @Test
    public void testSubjectPatFound() {
        String result = org.restscs.imp.Pat.subject("hello world", "world");
        assertEquals("1", result);
    }

    @Test
    public void testSubjectReversePatFound() {
        String result = org.restscs.imp.Pat.subject("hello dlrow", "world");
        assertEquals("2", result);
    }

    @Test
    public void testSubjectBothPatAndReverseFound() {
        String result = org.restscs.imp.Pat.subject("hello world dlrow", "world");
        assertEquals("1", result);
    }

    @Test
    public void testSubjectPalindromePatReversePat() {
        String result = org.restscs.imp.Pat.subject("hello worlddlrow", "world");
        assertEquals("1", result);
    }

    @Test
    public void testSubjectPalindromeReversePatPat() {
        String result = org.restscs.imp.Pat.subject("hello dlrowworld", "world");
        assertEquals("2", result);
    }

    @Test
    public void testSubjectShortPat() {
        String result = org.restscs.imp.Pat.subject("hello", "he");
        assertEquals("0", result);
    }

    @Test
    @Ignore
    public void testPatEndpoint() {
        given()
            .baseUri(baseUrlOfSut)
            .accept("*/*")
            .get("/api/pat/{txt}/{pat}", "hello world", "world")
            .then()
            .statusCode(200)
            .body(equalTo("1"));
    }

    @Test
    @Ignore
    public void testPatEndpointReverseFound() {
        given()
            .baseUri(baseUrlOfSut)
            .accept("*/*")
            .get("/api/pat/{txt}/{pat}", "hello dlrow", "world")
            .then()
            .statusCode(200)
            .body(equalTo("2"));
    }

    @Test
    public void testPatEndpointNotFound() {
        given()
            .baseUri(baseUrlOfSut)
            .accept("*/*")
            .get("/api/pat/{txt}/{pat}", "hello", "xyz")
            .then()
            .statusCode(200)
            .body(equalTo("0"));
    }
}
