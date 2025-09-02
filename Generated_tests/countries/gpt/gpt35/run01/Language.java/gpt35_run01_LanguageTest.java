
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

public class gpt35_run01_LanguageTest {

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

    // Placeholder class for Language
    private static class Language {
        private String iso639_1;
        private String iso639_2;
        private String name;
        private String nativeName;

        public void setIso639_1(String iso639_1) {
            this.iso639_1 = iso639_1;
        }

        public String getIso639_1() {
            return iso639_1;
        }

        public void setIso639_2(String iso639_2) {
            this.iso639_2 = iso639_2;
        }

        public String getIso639_2() {
            return iso639_2;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setNativeName(String nativeName) {
            this.nativeName = nativeName;
        }

        public String getNativeName() {
            return nativeName;
        }
    }

    @Test
    public void testSetIso639_1() {
        Language language = new Language();
        language.setIso639_1("en");
        assertEquals("en", language.getIso639_1());
    }

    @Test
    public void testSetIso639_2() {
        Language language = new Language();
        language.setIso639_2("eng");
        assertEquals("eng", language.getIso639_2());
    }

    @Test
    public void testSetName() {
        Language language = new Language();
        language.setName("English");
        assertEquals("English", language.getName());
    }

    @Test
    public void testSetNativeName() {
        Language language = new Language();
        language.setNativeName("English");
        assertEquals("English", language.getNativeName());
    }

}
  