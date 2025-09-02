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

public class sonnet35_run01_CountryServiceBaseTest {

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
    public void testGetByCodeList() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/alpha?codes=US;CA;MX")
            .then()
            .statusCode(200)
            .body("size()", equalTo(3))
            .body("[0].alpha2Code", equalTo("US"))
            .body("[1].alpha2Code", equalTo("CA"))
            .body("[2].alpha2Code", equalTo("MX"));
    }

    @Test
    public void testGetByCodeListWithInvalidCode() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/alpha?codes=US;XX;CA")
            .then()
            .statusCode(200)
            .body("size()", equalTo(2))
            .body("[0].alpha2Code", equalTo("US"))
            .body("[1].alpha2Code", equalTo("CA"));
    }

    @Test
    public void testGetByCodeListWithNullInput() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/alpha")
            .then()
            .statusCode(400);
    }

    @Test
    public void testFulltextSearch() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/name/united?fullText=true")
            .then()
            .statusCode(200)
            .body("size()", equalTo(2))
            .body("[0].name", equalTo("United Arab Emirates"))
            .body("[1].name", equalTo("United Kingdom of Great Britain and Northern Ireland"));
    }

    @Test
    public void testFulltextSearchWithAlternativeSpelling() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/name/schweiz?fullText=true")
            .then()
            .statusCode(200)
            .body("size()", equalTo(1))
            .body("[0].name", equalTo("Switzerland"));
    }

    @Test
    public void testFulltextSearchWithNoMatch() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/name/nonexistentcountry?fullText=true")
            .then()
            .statusCode(404);
    }

    @Test
    public void testLoadJson() {
        given()
            .accept("application/json")
            .get(baseUrlOfSut + "/v2/all")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    @Test
    public void testConstructor() {
        eu.fayder.restcountries.rest.CountryServiceBase countryServiceBase = new eu.fayder.restcountries.rest.CountryServiceBase();
        assertNotNull(countryServiceBase);
    }
}