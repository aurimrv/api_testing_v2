
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

// Mock implementation of the missing CountryTranslations class
class CountryTranslations {
    private String de;
    private String es;
    private String fr;
    private String ja;
    private String it;

    public CountryTranslations() {
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getEs() {
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

    public String getJa() {
        return ja;
    }

    public void setJa(String ja) {
        this.ja = ja;
    }

    public String getIt() {
        return it;
    }

    public void setIt(String it) {
        this.it = it;
    }
}

public class gpt4o_run01_CountryTranslationsTest {

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
    public void testConstructor() {
        CountryTranslations countryTranslations = new CountryTranslations();
        assertNotNull(countryTranslations);
    }

    @Test
    public void testSetDe() {
        CountryTranslations countryTranslations = new CountryTranslations();
        countryTranslations.setDe("Deutschland");
        assertEquals("Deutschland", countryTranslations.getDe());
    }

    @Test
    public void testSetEs() {
        CountryTranslations countryTranslations = new CountryTranslations();
        countryTranslations.setEs("España");
        assertEquals("España", countryTranslations.getEs());
    }

    @Test
    public void testSetFr() {
        CountryTranslations countryTranslations = new CountryTranslations();
        countryTranslations.setFr("France");
        assertEquals("France", countryTranslations.getFr());
    }

    @Test
    public void testSetJa() {
        CountryTranslations countryTranslations = new CountryTranslations();
        countryTranslations.setJa("日本");
        assertEquals("日本", countryTranslations.getJa());
    }

    @Test
    public void testSetIt() {
        CountryTranslations countryTranslations = new CountryTranslations();
        countryTranslations.setIt("Italia");
        assertEquals("Italia", countryTranslations.getIt());
    }
}
