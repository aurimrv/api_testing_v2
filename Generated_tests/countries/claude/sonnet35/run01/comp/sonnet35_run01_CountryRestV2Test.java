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

public class sonnet35_run01_CountryRestV2Test {

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
            .get(baseUrlOfSut + "/v2/alpha/US")
            .then()
            .statusCode(200)
            .body("alpha2Code", equalTo("US"));

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/alpha/USA")
            .then()
            .statusCode(200)
            .body("alpha3Code", equalTo("USA"));

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/alpha/INVALID")
            .then()
            .statusCode(400);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/alpha/ZZZ")
            .then()
            .statusCode(404);
    }

    @Test
    public void testGetByAlphaList() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/alpha?codes=US;CA;MX")
            .then()
            .statusCode(200)
            .body("size()", equalTo(3));

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/alpha?codes=INVALID")
            .then()
            .statusCode(400);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/alpha?codes=ZZZ")
            .then()
            .statusCode(404);
    }

    @Test
    public void testGetByCurrency() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/currency/USD")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0));

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/currency/INVALID")
            .then()
            .statusCode(400);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/currency/ZZZ")
            .then()
            .statusCode(404);
    }

    @Test
    public void testGetByName() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/name/United")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0));

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/name/United States")
            .then()
            .statusCode(200)
            .body("size()", equalTo(1));

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/name/ZZZ")
            .then()
            .statusCode(404);
    }

    @Test
    public void testGetByCallingCode() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/callingcode/1")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0));

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/callingcode/999")
            .then()
            .statusCode(404);
    }

    @Test
    public void testGetByCapital() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/capital/Washington")
            .then()
            .statusCode(200)
            .body("size()", equalTo(1));

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/capital/ZZZ")
            .then()
            .statusCode(404);
    }

    @Test
    public void testGetByRegion() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/region/Europe")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0));

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/region/ZZZ")
            .then()
            .statusCode(404);
    }

    @Test
    public void testGetBySubRegion() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/subregion/Northern Europe")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0));

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/subregion/ZZZ")
            .then()
            .statusCode(404);
    }

    @Test
    public void testGetByLanguage() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/lang/en")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0));

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/lang/ZZZ")
            .then()
            .statusCode(404);
    }

    @Test
    public void testGetByDemonym() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/demonym/American")
            .then()
            .statusCode(200)
            .body("size()", equalTo(1));

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/demonym/ZZZ")
            .then()
            .statusCode(404);
    }

    @Test
    public void testGetByRegionalBloc() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/regionalbloc/EU")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0));

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/regionalbloc/ZZZ")
            .then()
            .statusCode(404);
    }

    @Test
    public void testDoPOST() {
        given()
            .accept("application/json")
            .post(baseUrlOfSut + "/v2")
            .then()
            .statusCode(405);
    }

    @Test
    public void testIsEmpty() {
        // This method is private, so we test it indirectly through public methods
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/alpha/")
            .then()
            .statusCode(400);

        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/alpha/ ")
            .then()
            .statusCode(400);
    }
}
