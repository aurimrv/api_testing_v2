
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

public class gpt35_run01_ProjectServiceImplTest {

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
    public void testFindProjects() {
        // Test coverage for findProjects method
        // Implement your test for findProjects method here
    }

    @Test
    public void testGetMergedProjectList() {
        // Test coverage for getMergedProjectList method
        // Implement your test for getMergedProjectList method here
    }

    @Test
    public void testCreateMergedProject() {
        // Test coverage for createMergedProject method
        // Implement your test for createMergedProject method here
    }

    @Test
    public void testConvertProjectsToMap() {
        // Test coverage for convertProjectsToMap method
        // Implement your test for convertProjectsToMap method here
    }

    @Test
    public void testGetSortedResultList() {
        // Test coverage for getSortedResultList method
        // Implement your test for getSortedResultList method here
    }

    @Test
    public void testGetOrganizationConfig() {
        // Test coverage for getOrganizationConfig method
        // Implement your test for getOrganizationConfig method here
    }

    @Test
    public void testIsSortOrderAscending() {
        // Test coverage for isSortOrderAscending method
        // Implement your test for isSortOrderAscending method here
    }

    @Test
    public void testGetProjectSortComparator() {
        // Test coverage for getProjectSortComparator method
        // Implement your test for getProjectSortComparator method here
    }
}
