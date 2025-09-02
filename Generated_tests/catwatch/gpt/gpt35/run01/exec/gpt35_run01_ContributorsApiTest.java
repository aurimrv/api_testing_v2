
package org.zalando.catwatch.backend;

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
import static org.evomaster.client.java.sql.dsl.SqlDsl.sql;
import org.evomaster.client.java.controller.api.dto.database.operations.InsertionResultsDto;
import org.evomaster.client.java.controller.api.dto.database.operations.InsertionDto;
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

public class gpt35_run01_ContributorsApiTest {

    private static final SutHandler controller = new em.embedded.org.zalando.EmbeddedEvoMasterController();
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
        controller.resetDatabase(Arrays.asList("CONTRIBUTOR"));
        controller.resetStateOfSUT();
    }

    @Test
    public void testContributorsGet() {
        // Test for contributorsGet method

    }

    @Test
    public void testContributorsGet_noTimeSpan() {
        // Test for contributorsGet_noTimeSpan method

    }

    @Test
    public void testLambdaContributorsGet_noTimeSpan() {
        // Test for lambda$contributorsGet_noTimeSpan$1 method
        
    }

    @Test
    public void testLambdaNull() {
        // Test for lambda$null$0 method

    }

    @Test
    public void testLambdaContributorsGet_noTimeSpan2() {
        // Test for lambda$contributorsGet_noTimeSpan$2 method
        
    }

    @Test
    public void testContributorsGet_timeSpan() {
        // Test for contributorsGet_timeSpan method

    }

    @Test
    public void testLambdaContributorsGet_timeSpan5() {
        // Test for lambda$contributorsGet_timeSpan$5 method

    }

    @Test
    public void testLambdaNull3() {
        // Test for lambda$null$3 method

    }

    @Test
    public void testLambdaNull4() {
        // Test for lambda$null$4 method

    }

    @Test
    public void testLambdaContributorsGet_timeSpan6() {
        // Test for lambda$contributorsGet_timeSpan$6 method

    }

    @Test
    public void testSublist() {
        // Test for sublist method

    }

    @Test
    public void testValidate() {
        // Test for validate method

    }

    @Test
    public void testSortBy() {
        // Test for sortBy method

    }

    @Test
    public void testAdd() {
        // Test for add method

    }

    @Test
    public void testDiff() {
        // Test for diff method

    }

    @Test
    public void testSubtract() {
        // Test for subtract method

    }

    @Test
    public void testComparator() {
        // Test for comparator method

    }
}
  