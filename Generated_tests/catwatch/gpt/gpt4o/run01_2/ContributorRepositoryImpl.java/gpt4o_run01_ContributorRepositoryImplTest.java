
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
import org.zalando.catwatch.backend.repo.ContributorRepositoryImpl; // Added import for ContributorRepositoryImpl

public class gpt4o_run01_ContributorRepositoryImplTest {

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
    public void testFindAllTimeTopContributors_NullOrganizationId() {
        Long organizationId = null;
        String snapshotDate = "2023-01-01T00:00:00.000Z";
        String namePrefix = null;
        Integer offset = 0;
        Integer limit = 10;

        ValidatableResponse response = given()
            .queryParam("organizationId", organizationId)
            .queryParam("snapshotDate", snapshotDate)
            .queryParam("namePrefix", namePrefix)
            .queryParam("offset", offset)
            .queryParam("limit", limit)
            .get(baseUrlOfSut + "/contributors")
            .then();

        response.statusCode(400);
        response.body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testFindAllTimeTopContributors_WithOrganizationIdAndNamePrefix() {
        Long organizationId = 1L;
        String snapshotDate = "2023-01-01T00:00:00.000Z";
        String namePrefix = "John";
        Integer offset = 0;
        Integer limit = 10;

        ValidatableResponse response = given()
            .queryParam("organizationId", organizationId)
            .queryParam("snapshotDate", snapshotDate)
            .queryParam("namePrefix", namePrefix)
            .queryParam("offset", offset)
            .queryParam("limit", limit)
            .get(baseUrlOfSut + "/contributors")
            .then();

        response.statusCode(400);
        response.body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testFindContributorsTimeSeries_WithDateRange() {
        Long organizationId = 1L;
        String startDate = "2023-01-01T00:00:00.000Z";
        String endDate = "2023-12-31T23:59:59.999Z";
        String namePrefix = null;

        ValidatableResponse response = given()
            .queryParam("organizationId", organizationId)
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .queryParam("namePrefix", namePrefix)
            .get(baseUrlOfSut + "/statistics/contributors")
            .then();

        response.statusCode(500);
        response.body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testFindContributorsTimeSeries_WithoutStartDate() {
        Long organizationId = 1L;
        String endDate = "2023-12-31T23:59:59.999Z";
        String namePrefix = null;

        ValidatableResponse response = given()
            .queryParam("organizationId", organizationId)
            .queryParam("endDate", endDate)
            .queryParam("namePrefix", namePrefix)
            .get(baseUrlOfSut + "/statistics/contributors")
            .then();

        response.statusCode(500);
        response.body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testFindContributorsTimeSeries_WithNamePrefix() {
        Long organizationId = 1L;
        String startDate = "2023-01-01T00:00:00.000Z";
        String endDate = "2023-12-31T23:59:59.999Z";
        String namePrefix = "Jane";

        ValidatableResponse response = given()
            .queryParam("organizationId", organizationId)
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .queryParam("namePrefix", namePrefix)
            .get(baseUrlOfSut + "/statistics/contributors")
            .then();

        response.statusCode(500);
        response.body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testConstructorExecution() {
        ContributorRepositoryImpl instance = new ContributorRepositoryImpl();
        assertNotNull(instance);
    }
}
