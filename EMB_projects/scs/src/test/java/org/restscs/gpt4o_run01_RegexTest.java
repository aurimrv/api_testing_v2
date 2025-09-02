
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

public class gpt4o_run01_RegexTest {

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

    // Testing the constructor for coverage
    @Test
    public void testConstructor() {
        assertNotNull(new org.restscs.imp.Regex());
    }

    // Test for URL pattern
    @Test
    public void testSubjectUrl() {
        String input = "http://example/path";
        String result = org.restscs.imp.Regex.subject(input);
        assertEquals("url", result);
    }

    // Test for valid date pattern
    @Test
    public void testSubjectDate() {
        String input = "mon12mar";
        String result = org.restscs.imp.Regex.subject(input);
        assertEquals("date", result);
    }

    // Test for valid floating-point number with exponent pattern
    @Test
    public void testSubjectFpe() {
        String input = "12.34e+56";
        String result = org.restscs.imp.Regex.subject(input);
        assertEquals("fpe", result);
    }

    // Test for input that matches none of the patterns
    @Test
    public void testSubjectNone() {
        String input = "invalidpattern";
        String result = org.restscs.imp.Regex.subject(input);
        assertEquals("none", result);
    }

    // Test for a case with slightly malformed URL
    @Test
    public void testSubjectMalformedUrl() {
        String input = "http:/example/path";
        String result = org.restscs.imp.Regex.subject(input);
        assertEquals("none", result);
    }

    // Test for invalid date input
    @Test
    public void testSubjectInvalidDate() {
        String input = "mon32mar";
        String result = org.restscs.imp.Regex.subject(input);
        assertEquals("none", result);
    }

    // Test for invalid floating-point input
    @Test
    public void testSubjectInvalidFpe() {
        String input = "12.34e++56";
        String result = org.restscs.imp.Regex.subject(input);
        assertEquals("none", result);
    }
}
