
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

public class gpt35_run01_CookieTest {

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
    public void testCookieConstructor() {
        org.restscs.imp.Cookie cookie = new org.restscs.imp.Cookie();
        assertNotNull(cookie);
    }

    @Test
    public void testSubjectUserIdValLengthLessThan6() {
        ValidatableResponse response = given()
            .baseUri(baseUrlOfSut)
            .when()
            .get("/api/cookie/userid/abc/def")
            .then()
            .statusCode(200)
            .body(equalTo("0"));
    }

    @Test
    public void testSubjectUserIdValLengthGreaterThan6() {
        ValidatableResponse response = given()
            .baseUri(baseUrlOfSut)
            .when()
            .get("/api/cookie/userid/user123/abc.com")
            .then()
            .statusCode(200)
            .body(equalTo("1"));
    }

    @Test
    public void testSubjectSessionAmAbcCom() {
        ValidatableResponse response = given()
            .baseUri(baseUrlOfSut)
            .when()
            .get("/api/cookie/session/am/abc.com")
            .then()
            .statusCode(200)
            .body(equalTo("1"));
    }

    @Test
    public void testSubjectSessionNotAmAbcCom() {
        ValidatableResponse response = given()
            .baseUri(baseUrlOfSut)
            .when()
            .get("/api/cookie/session/notam/abc.com")
            .then()
            .statusCode(200)
            .body(equalTo("2"));
    }
}
