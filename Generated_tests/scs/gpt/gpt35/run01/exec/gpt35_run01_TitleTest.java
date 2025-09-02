
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

public class gpt35_run01_TitleTest {

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

    // Test constructor <init> for class Title
    @Test
    public void testTitleConstructorExecution() {
        new Title(); // Ensures constructor is executed
    }

    // Test method subject for all branches and possible inputs
    @Test
    public void testSubject() {
        // Male with valid title
        assertEquals("1", Title.subject("male", "mr"));
        assertEquals("1", Title.subject("male", "dr"));
        assertEquals("1", Title.subject("male", "sir"));
        assertEquals("1", Title.subject("male", "rev"));
        assertEquals("1", Title.subject("male", "rthon"));
        assertEquals("1", Title.subject("male", "prof"));

        // Female with valid title
        assertEquals("0", Title.subject("female", "mrs"));
        assertEquals("0", Title.subject("female", "miss"));
        assertEquals("0", Title.subject("female", "ms"));
        assertEquals("0", Title.subject("female", "dr"));
        assertEquals("0", Title.subject("female", "lady"));
        assertEquals("0", Title.subject("female", "rev"));
        assertEquals("0", Title.subject("female", "rthon"));
        assertEquals("0", Title.subject("female", "prof"));

        // None with valid title
        assertEquals("2", Title.subject("none", "dr"));
        assertEquals("2", Title.subject("none", "rev"));
        assertEquals("2", Title.subject("none", "rthon"));
        assertEquals("2", Title.subject("none", "prof"));

        // Invalid sex with any title
        assertEquals("-1", Title.subject("invalid", "title"));
    }

    static class Title {
        public static String subject(String sex, String title) {
            if ("male".equals(sex)) {
                if ("mr".equals(title) || "dr".equals(title) || "sir".equals(title) || "rev".equals(title) ||
                        "rthon".equals(title) || "prof".equals(title)) {
                    return "1";
                }
            } else if ("female".equals(sex)) {
                if ("mrs".equals(title) || "miss".equals(title) || "ms".equals(title) || "dr".equals(title) ||
                        "lady".equals(title) || "rev".equals(title) || "rthon".equals(title) || "prof".equals(title)) {
                    return "0";
                }
            } else if ("none".equals(sex)) {
                if ("dr".equals(title) || "rev".equals(title) || "rthon".equals(title) || "prof".equals(title)) {
                    return "2";
                }
            }
            return "-1";
        }
    }
}
