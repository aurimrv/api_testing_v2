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

public class sonnet35_run01_CountryTranslationsTest {

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

    private static class CountryTranslations {
        private String de;
        private String es;
        private String fr;
        private String ja;
        private String it;

        public void setDe(String de) { this.de = de; }
        public String getDe() { return de; }
        public void setEs(String es) { this.es = es; }
        public String getEs() { return es; }
        public void setFr(String fr) { this.fr = fr; }
        public String getFr() { return fr; }
        public void setJa(String ja) { this.ja = ja; }
        public String getJa() { return ja; }
        public void setIt(String it) { this.it = it; }
        public String getIt() { return it; }
    }

    @Test
    public void testSetDe() {
        CountryTranslations translations = new CountryTranslations();
        String germanTranslation = "Deutschland";
        translations.setDe(germanTranslation);
        assertEquals(germanTranslation, translations.getDe());
    }

    @Test
    public void testSetEs() {
        CountryTranslations translations = new CountryTranslations();
        String spanishTranslation = "España";
        translations.setEs(spanishTranslation);
        assertEquals(spanishTranslation, translations.getEs());
    }

    @Test
    public void testSetFr() {
        CountryTranslations translations = new CountryTranslations();
        String frenchTranslation = "France";
        translations.setFr(frenchTranslation);
        assertEquals(frenchTranslation, translations.getFr());
    }

    @Test
    public void testSetJa() {
        CountryTranslations translations = new CountryTranslations();
        String japaneseTranslation = "日本";
        translations.setJa(japaneseTranslation);
        assertEquals(japaneseTranslation, translations.getJa());
    }

    @Test
    public void testSetIt() {
        CountryTranslations translations = new CountryTranslations();
        String italianTranslation = "Italia";
        translations.setIt(italianTranslation);
        assertEquals(italianTranslation, translations.getIt());
    }

    @Test
    public void testConstructor() {
        CountryTranslations translations = new CountryTranslations();
        assertNotNull(translations);
    }

    @Test
    public void testAllSettersAndGetters() {
        CountryTranslations translations = new CountryTranslations();
        
        translations.setDe("Deutschland");
        translations.setEs("España");
        translations.setFr("France");
        translations.setJa("日本");
        translations.setIt("Italia");

        assertEquals("Deutschland", translations.getDe());
        assertEquals("España", translations.getEs());
        assertEquals("France", translations.getFr());
        assertEquals("日本", translations.getJa());
        assertEquals("Italia", translations.getIt());
    }

    @Test
    public void testSettersWithNullValues() {
        CountryTranslations translations = new CountryTranslations();
        
        translations.setDe(null);
        translations.setEs(null);
        translations.setFr(null);
        translations.setJa(null);
        translations.setIt(null);

        assertNull(translations.getDe());
        assertNull(translations.getEs());
        assertNull(translations.getFr());
        assertNull(translations.getJa());
        assertNull(translations.getIt());
    }

    @Test
    public void testSettersWithEmptyStrings() {
        CountryTranslations translations = new CountryTranslations();
        
        translations.setDe("");
        translations.setEs("");
        translations.setFr("");
        translations.setJa("");
        translations.setIt("");

        assertEquals("", translations.getDe());
        assertEquals("", translations.getEs());
        assertEquals("", translations.getFr());
        assertEquals("", translations.getJa());
        assertEquals("", translations.getIt());
    }
}
