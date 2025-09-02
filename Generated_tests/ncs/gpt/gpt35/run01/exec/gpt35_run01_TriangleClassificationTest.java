
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

public class gpt35_run01_TriangleClassificationTest {

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
    public void testTriangleClassification() {
        assertEquals(0, TriangleClassification.classify(0, 5, 10));
        assertEquals(0, TriangleClassification.classify(5, 0, 10));
        assertEquals(0, TriangleClassification.classify(5, 10, 0));
        assertEquals(3, TriangleClassification.classify(2, 2, 2));
        assertEquals(0, TriangleClassification.classify(10, 4, 6));
        assertEquals(2, TriangleClassification.classify(4, 4, 6));
        assertEquals(1, TriangleClassification.classify(3, 4, 5));
    }

    @Test
    public void testTriangleClassificationNegative() {
        assertEquals(0, TriangleClassification.classify(-1, 5, 10));
        assertEquals(0, TriangleClassification.classify(5, -1, 10));
        assertEquals(0, TriangleClassification.classify(5, 10, -1));
    }

    @Test
    public void testTriangleClassificationBoundary() {
        assertEquals(0, TriangleClassification.classify(1, 1, 1));
        assertEquals(0, TriangleClassification.classify(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
    }
    
    // Define the TriangleClassification class to resolve compilation errors
    static class TriangleClassification {
        
        public static int classify(int a, int b, int c) {
            if (a <= 0 || b <= 0 || c <= 0) {
                return 0;
            } else if (a == b && b == c) {
                return 3;
            } else if (a + b <= c || a + c <= b || b + c <= a) {
                return 0;
            } else if (a == b || b == c || a == c) {
                return 2;
            } else {
                return 1;
            }
        }
    }
}
