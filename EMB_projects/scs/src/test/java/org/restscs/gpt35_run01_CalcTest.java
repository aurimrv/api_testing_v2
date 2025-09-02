
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

public class gpt35_run01_CalcTest {

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
    public void testCalcConstPi() {
        ValidatableResponse response = given()
                .basePath("/api/calc/pi/0.0/0.0")
                .when()
                .get(baseUrlOfSut)
                .then()
                .statusCode(200);
        String result = response.extract().asString();
        assertEquals("3.141592653589793", result);
    }

    @Test
    public void testCalcUnarySqrt() {
        ValidatableResponse response = given()
                .basePath("/api/calc/sqrt/16.0/0.0")
                .when()
                .get(baseUrlOfSut)
                .then()
                .statusCode(200);
        String result = response.extract().asString();
        assertEquals("4.0", result);
    }

    @Test
    public void testCalcBinaryMultiply() {
        ValidatableResponse response = given()
                .basePath("/api/calc/multiply/5.0/3.0")
                .when()
                .get(baseUrlOfSut)
                .then()
                .statusCode(200);
        String result = response.extract().asString();
        assertEquals("15.0", result);
    }

    @Test
    public void testCalcBinaryDivide() {
        ValidatableResponse response = given()
                .basePath("/api/calc/divide/10.0/2.0")
                .when()
                .get(baseUrlOfSut)
                .then()
                .statusCode(200);
        String result = response.extract().asString();
        assertEquals("5.0", result);
    }

    @Test
    public void testCalcBinarySubtract() {
        ValidatableResponse response = given()
                .basePath("/api/calc/subtract/8.0/3.0")
                .when()
                .get(baseUrlOfSut)
                .then()
                .statusCode(200);
        String result = response.extract().asString();
        assertEquals("5.0", result);
    }

}
