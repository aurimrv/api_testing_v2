
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

public class gpt4o_run01_DatabasePopulatorTest {

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
    public void testDatabasePopulatorConstructor() {
        // Explicitly test the constructor to ensure it executes
        new org.zalando.catwatch.backend.repo.util.DatabasePopulator(
                null, null, null, null
        );
    }

    @Test
    public void testPopulateTestDataDatabaseUnavailable() {
        // Simulate the database being unavailable
        given().baseUri(baseUrlOfSut)
                .when()
                .get("/simulateDatabaseDown") // Simulated endpoint for testing
                .then()
                .statusCode(200);

        org.zalando.catwatch.backend.repo.util.DatabasePopulator populator =
                new org.zalando.catwatch.backend.repo.util.DatabasePopulator(null, null, null, null);

        populator.populateTestData(); // Should exit early, no operations performed

        given().baseUri(baseUrlOfSut)
                .when()
                .get("/contributors") // Ensure no contributor records exist
                .then()
                .statusCode(200)
                .body("$.size()", equalTo(0));
    }

    @Test
    public void testPopulateTestDataCompleteFlow() {
        // Simulate that the database is available
        given().baseUri(baseUrlOfSut)
                .when()
                .get("/simulateDatabaseUp") // Simulated endpoint for testing
                .then()
                .statusCode(200);

        org.zalando.catwatch.backend.repo.util.DatabasePopulator populator =
                new org.zalando.catwatch.backend.repo.util.DatabasePopulator(null, null, null, null);

        populator.populateTestData(); // Execute the full flow

        // Verify assertions for contributors
        given().baseUri(baseUrlOfSut)
                .queryParam("organizations", "galanto")
                .when()
                .get("/contributors")
                .then()
                .statusCode(200)
                .body("$.size()", greaterThanOrEqualTo(2)); // Ensure contributors were created
    }
}
