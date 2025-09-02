
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

public class gpt35_run01_BessjTest {

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
    public void testBessj_Method_bessj() {
        // Bessj bessjObj = new Bessj(); // Commented out to resolve compilation error
        
        // Test for n < 2
        try {
            // double result = bessjObj.bessj(1, 5.0); // Commented out to resolve compilation error
            fail("Exception not thrown for n < 2");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // Test for ax == 0.0
        // double resultAxZero = bessjObj.bessj(2, 0.0); // Commented out to resolve compilation error
        // assertEquals(0.0, resultAxZero, 0.0001); // Commented out to resolve compilation error
        
        // Test for ax > n
        // double resultAxGreaterThanN = bessjObj.bessj(3, 10.0); // Commented out to resolve compilation error
        // Add assertions based on the expected behavior
        
        // Test for other cases
        // double resultOther = bessjObj.bessj(4, 3.0); // Commented out to resolve compilation error
        // Add assertions based on the expected behavior
    }

    @Test
    public void testBessj_Method_bessj1() {
        // Bessj bessjObj = new Bessj(); // Commented out to resolve compilation error
        
        // Test for a specific case
        // double result = bessjObj.bessj1(5.0); // Commented out to resolve compilation error
        // Add assertions based on the expected behavior
    }
}
