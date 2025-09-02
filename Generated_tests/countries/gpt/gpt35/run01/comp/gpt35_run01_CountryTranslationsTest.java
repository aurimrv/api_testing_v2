
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

public class gpt35_run01_CountryTranslationsTest {

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

    // Test for setDe method
    @Test
    public void testSetDe() {
        CountryTranslations countryTranslations = new CountryTranslations();
        String de = "Germany";
        countryTranslations.setDe(de);
        assertEquals(de, countryTranslations.getDe());
    }

    // Test for setEs method
    @Test
    public void testSetEs() {
        CountryTranslations countryTranslations = new CountryTranslations();
        String es = "Spain";
        countryTranslations.setEs(es);
        assertEquals(es, countryTranslations.getEs());
    }

    // Test for setFr method
    @Test
    public void testSetFr() {
        CountryTranslations countryTranslations = new CountryTranslations();
        String fr = "France";
        countryTranslations.setFr(fr);
        assertEquals(fr, countryTranslations.getFr());
    }

    // Test for setJa method
    @Test
    public void testSetJa() {
        CountryTranslations countryTranslations = new CountryTranslations();
        String ja = "Japan";
        countryTranslations.setJa(ja);
        assertEquals(ja, countryTranslations.getJa());
    }

    // Test for setIt method
    @Test
    public void testSetIt() {
        CountryTranslations countryTranslations = new CountryTranslations();
        String it = "Italy";
        countryTranslations.setIt(it);
        assertEquals(it, countryTranslations.getIt());
    }

    private class CountryTranslations {
        private String de;
        private String es;
        private String fr;
        private String ja;
        private String it;

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
}
