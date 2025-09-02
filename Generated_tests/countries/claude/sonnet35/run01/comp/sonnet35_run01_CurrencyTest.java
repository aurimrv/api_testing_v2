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

import eu.fayder.restcountries.v2.domain.Currency;

public class sonnet35_run01_CurrencyTest {

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
    public void testSetCode() {
        Currency currency = new Currency();
        String testCode = "USD";
        currency.setCode(testCode);
        assertEquals(testCode, currency.getCode());
    }

    @Test
    public void testSetName() {
        Currency currency = new Currency();
        String testName = "US Dollar";
        currency.setName(testName);
        assertEquals(testName, currency.getName());
    }

    @Test
    public void testSetSymbol() {
        Currency currency = new Currency();
        String testSymbol = "$";
        currency.setSymbol(testSymbol);
        assertEquals(testSymbol, currency.getSymbol());
    }

    @Test
    public void testConstructor() {
        Currency currency = new Currency();
        assertNull(currency.getCode());
        assertNull(currency.getName());
        assertNull(currency.getSymbol());
    }

    @Test
    public void testSetAndGetAllFields() {
        Currency currency = new Currency();
        String testCode = "EUR";
        String testName = "Euro";
        String testSymbol = "€";

        currency.setCode(testCode);
        currency.setName(testName);
        currency.setSymbol(testSymbol);

        assertEquals(testCode, currency.getCode());
        assertEquals(testName, currency.getName());
        assertEquals(testSymbol, currency.getSymbol());
    }

    @Test
    public void testSetNullValues() {
        Currency currency = new Currency();
        currency.setCode(null);
        currency.setName(null);
        currency.setSymbol(null);

        assertNull(currency.getCode());
        assertNull(currency.getName());
        assertNull(currency.getSymbol());
    }

    @Test
    public void testSetEmptyStrings() {
        Currency currency = new Currency();
        currency.setCode("");
        currency.setName("");
        currency.setSymbol("");

        assertEquals("", currency.getCode());
        assertEquals("", currency.getName());
        assertEquals("", currency.getSymbol());
    }
}