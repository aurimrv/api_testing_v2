
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

public class gpt35_run01_GammqTest {

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
    public void testGcf() {
        org.restncs.imp.Gammq instance = new org.restncs.imp.Gammq();
        
        // Test when a = 1.0 and x = 2.0
        // Fix compilation error by using reflection to access private method gcf
        try {
            java.lang.reflect.Method method = org.restncs.imp.Gammq.class.getDeclaredMethod("gcf", double.class, double.class);
            method.setAccessible(true);
            method.invoke(instance, 1.0, 2.0);
        } catch (Exception e) {
            fail("Exception occurred while invoking private method gcf: " + e.getMessage());
        }
        
        // Additional test cases can be added to cover different scenarios
    }

    @Test
    public void testGser() {
        org.restncs.imp.Gammq instance = new org.restncs.imp.Gammq();
        
        // Test when a = 2.0 and x = 3.0
        // Fix compilation error by using reflection to access private method gser
        try {
            java.lang.reflect.Method method = org.restncs.imp.Gammq.class.getDeclaredMethod("gser", double.class, double.class);
            method.setAccessible(true);
            method.invoke(instance, 2.0, 3.0);
        } catch (Exception e) {
            fail("Exception occurred while invoking private method gser: " + e.getMessage());
        }
        
        // Additional test cases can be added to cover different scenarios
    }

}
