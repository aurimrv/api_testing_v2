package eu.fayder.restcountries;

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

public class sonnet35_run01_CountryRestV1Test {

    private static final SutHandler controller = new em.embedded.eu.fayder.EmbeddedEvoMasterController();
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
    public void testGetByAlpha() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/alpha/US")
            .then()
            .statusCode(200);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/alpha/USA")
            .then()
            .statusCode(200);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/alpha/INVALID")
            .then()
            .statusCode(400);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/alpha/")
            .then()
            .statusCode(400);
    }

    @Test
    public void testGetByAlphaList() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/alpha?codes=US;CA;MX")
            .then()
            .statusCode(200);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/alpha?codes=INVALID")
            .then()
            .statusCode(400);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/alpha?codes=")
            .then()
            .statusCode(400);
    }

    @Test
    public void testGetByCurrency() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/currency/USD")
            .then()
            .statusCode(200);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/currency/INVALID")
            .then()
            .statusCode(404);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/currency/")
            .then()
            .statusCode(400);
    }

    @Test
    public void testGetByName() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/name/United")
            .then()
            .statusCode(200);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/name/United?fullText=true")
            .then()
            .statusCode(200);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/name/INVALID")
            .then()
            .statusCode(404);
    }

    @Test
    public void testGetByCallingCode() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/callingcode/1")
            .then()
            .statusCode(200);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/callingcode/INVALID")
            .then()
            .statusCode(400);
    }

    @Test
    public void testGetByCapital() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/capital/Washington")
            .then()
            .statusCode(200);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/capital/INVALID")
            .then()
            .statusCode(404);
    }

    @Test
    public void testGetByRegion() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/region/Americas")
            .then()
            .statusCode(200);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/region/INVALID")
            .then()
            .statusCode(404);
    }

    @Test
    public void testGetBySubregion() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/subregion/Northern America")
            .then()
            .statusCode(200);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/subregion/INVALID")
            .then()
            .statusCode(404);
    }

    @Test
    public void testGetByLanguage() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/lang/en")
            .then()
            .statusCode(200);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/lang/INVALID")
            .then()
            .statusCode(404);
    }

    @Test
    public void testDoPOST() {
        given()
            .accept("application/json")
            .post(baseUrlOfSut + "/v1")
            .then()
            .statusCode(405);
    }

    @Test
    public void testIsEmpty() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/alpha/")
            .then()
            .statusCode(400);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v1/alpha?codes=")
            .then()
            .statusCode(400);
    }
}