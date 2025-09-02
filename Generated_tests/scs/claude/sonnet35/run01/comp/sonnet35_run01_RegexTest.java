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

public class sonnet35_run01_RegexTest {

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
    public void testSubjectWithUrl() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/pat/http://example.com/path")
            .then()
            .statusCode(200)
            .body(equalTo("url"));
    }

    @Test
    public void testSubjectWithDate() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/pat/mon01jan")
            .then()
            .statusCode(200)
            .body(equalTo("date"));
    }

    @Test
    public void testSubjectWithFpe() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/pat/123.45e-6")
            .then()
            .statusCode(200)
            .body(equalTo("fpe"));
    }

    @Test
    public void testSubjectWithNone() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/pat/notmatching")
            .then()
            .statusCode(200)
            .body(equalTo("none"));
    }

    @Test
    public void testSubjectWithEmptyString() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/pat/")
            .then()
            .statusCode(200)
            .body(equalTo("none"));
    }

    @Test
    public void testSubjectWithLongUrl() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/pat/http://very-long-domain-name-with-many-subdomains.example.com/path/to/resource")
            .then()
            .statusCode(200)
            .body(equalTo("url"));
    }

    @Test
    public void testSubjectWithInvalidUrl() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/pat/http:/invalid-url")
            .then()
            .statusCode(200)
            .body(equalTo("none"));
    }

    @Test
    public void testSubjectWithInvalidDate() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/pat/mon32jan")
            .then()
            .statusCode(200)
            .body(equalTo("none"));
    }

    @Test
    public void testSubjectWithInvalidFpe() {
        given()
            .accept("*/*")
            .get(baseUrlOfSut + "/api/pat/123.45e")
            .then()
            .statusCode(200)
            .body(equalTo("none"));
    }
}
