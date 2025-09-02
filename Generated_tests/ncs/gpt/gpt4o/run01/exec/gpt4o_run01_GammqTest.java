
package org.restncs;

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

public class gpt4o_run01_GammqTest {

    private static final SutHandler controller = new em.embedded.org.restncs.EmbeddedEvoMasterController();
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
    public void testGammqConstructor() {
        // Test to explicitly execute the constructor
        org.restncs.imp.Gammq gammq = new org.restncs.imp.Gammq();
        assertNotNull(gammq);
    }

    @Test
    public void testGcfSuccessfulExecution() {
        org.restncs.imp.Gammq gammq = new org.restncs.imp.Gammq();
        // Test a case where gcf completes without hitting the iteration limit
        double a = 2.5;
        double x = 5.0;
        // Using reflective call to ensure private method coverage
        try {
            java.lang.reflect.Method gcfMethod = gammq.getClass().getDeclaredMethod("gcf", double.class, double.class);
            gcfMethod.setAccessible(true);
            gcfMethod.invoke(gammq, a, x);
        } catch (Exception e) {
            fail("Exception during gcf execution: " + e.getMessage());
        }
    }

    @Test
    public void testGcfIterationLimitExceeded() {
        org.restncs.imp.Gammq gammq = new org.restncs.imp.Gammq();
        double a = 200.0; // High value to exceed iteration limit
        double x = 1.0;

        try {
            java.lang.reflect.Method gcfMethod = gammq.getClass().getDeclaredMethod("gcf", double.class, double.class);
            gcfMethod.setAccessible(true);
            gcfMethod.invoke(gammq, a, x);
            fail("Expected RuntimeException for iteration limit exceeded");
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof RuntimeException);
            assertEquals("a too large, ITMAX too small in gcf", e.getCause().getMessage());
        }
    }

    @Test
    public void testGserSuccessfulExecution() {
        org.restncs.imp.Gammq gammq = new org.restncs.imp.Gammq();
        double a = 2.0;
        double x = 1.0;

        try {
            java.lang.reflect.Method gserMethod = gammq.getClass().getDeclaredMethod("gser", double.class, double.class);
            gserMethod.setAccessible(true);
            gserMethod.invoke(gammq, a, x);
        } catch (Exception e) {
            fail("Exception during gser execution: " + e.getMessage());
        }
    }

    @Test
    public void testGserInputXLessThanZero() {
        org.restncs.imp.Gammq gammq = new org.restncs.imp.Gammq();
        double a = 2.0;
        double x = -1.0;

        try {
            java.lang.reflect.Method gserMethod = gammq.getClass().getDeclaredMethod("gser", double.class, double.class);
            gserMethod.setAccessible(true);
            gserMethod.invoke(gammq, a, x);
            fail("Expected RuntimeException for x < 0");
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof RuntimeException);
            assertEquals("x less than 0 in routine gser", e.getCause().getMessage());
        }
    }

    @Test
    public void testGserIterationLimitExceeded() {
        org.restncs.imp.Gammq gammq = new org.restncs.imp.Gammq();
        double a = 1.0;
        double x = 50.0; // Large value of x to exceed iteration limit

        try {
            java.lang.reflect.Method gserMethod = gammq.getClass().getDeclaredMethod("gser", double.class, double.class);
            gserMethod.setAccessible(true);
            gserMethod.invoke(gammq, a, x);
            fail("Expected RuntimeException for iteration limit exceeded");
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof RuntimeException);
            assertEquals("a too large, ITMAX too small in routine gser", e.getCause().getMessage());
        }
    }

    @Test
    public void testExeGserPath() {
        org.restncs.imp.Gammq gammq = new org.restncs.imp.Gammq();
        double a = 2.0;
        double x = 0.5;

        double result = gammq.exe(a, x);
        assertEquals(0.909795989739917, result, 1e-10);
    }

    @Test
    public void testExeGcfPath() {
        org.restncs.imp.Gammq gammq = new org.restncs.imp.Gammq();
        double a = 2.0;
        double x = 5.0;

        double result = gammq.exe(a, x);
        assertEquals(0.04042768199451279, result, 1e-10);
    }

    @Test
    public void testExeInvalidArguments() {
        org.restncs.imp.Gammq gammq = new org.restncs.imp.Gammq();
        try {
            gammq.exe(-1.0, 2.0); // Invalid 'a'
            fail("Expected RuntimeException for invalid arguments");
        } catch (RuntimeException e) {
            assertEquals("Invalid arguments in routine gammq", e.getMessage());
        }

        try {
            gammq.exe(2.0, -1.0); // Invalid 'x'
            fail("Expected RuntimeException for invalid arguments");
        } catch (RuntimeException e) {
            assertEquals("Invalid arguments in routine gammq", e.getMessage());
        }
    }
}
