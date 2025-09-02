
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

public class gpt35_run01_OrganizationWrapperTest {

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

    // Test <clinit> method
    @Test
    public void testClinit() {
        // Check if the class is initialized
        // Add specific tests for initialization if needed
    }

    // Test <init> method
    @Test
    public void testInitMethod() {
        // Add test cases for <init> method
        // Include boundary cases and exception handling
    }

    // Test lambda$new$0 method
    @Test
    public void testLambdaNew0() {
        // Add test cases for lambda$new$0 method
        // Cover all possible execution paths
    }

    // Test lambda$new$1 method
    @Test
    public void testLambdaNew1() {
        // Add test cases for lambda$new$1 method
        // Cover all possible execution paths
    }

    // Test lambda$new$2 method
    @Test
    public void testLambdaNew2() {
        // Add test cases for lambda$new$2 method
        // Cover all possible execution paths
    }

    // Test listTeams method
    @Test
    public void testListTeams() {
        // Add test cases for listTeams method
        // Cover all possible execution paths
    }

    // Test listMembers method
    @Test
    public void testListMembers() {
        // Add test cases for listMembers method
        // Cover all possible execution paths
    }

    // Test listRepositories method
    @Test
    public void testListRepositories() {
        // Add test cases for listRepositories method
        // Cover all possible execution paths
    }

    // Test getId method
    @Test
    public void testGetId() {
        // Add test cases for getId method
        // Cover all possible execution paths
    }

    // Test getPublicRepoCount method
    @Test
    public void testGetPublicRepoCount() {
        // Add test cases for getPublicRepoCount method
        // Cover all possible execution paths
    }

    // Test getLogin method
    @Test
    public void testGetLogin() {
        // Add test cases for getLogin method
        // Cover all possible execution paths
    }

    // Test contributorIsMember method
    @Test
    public void testContributorIsMember() {
        // Add test cases for contributorIsMember method
        // Cover all possible execution paths
    }
}
