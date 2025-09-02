
package org.zalando.catwatch.backend;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import static org.evomaster.client.java.controller.api.EMTestUtils.*;
import org.evomaster.client.java.controller.SutHandler;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.ValidatableResponse;
import static org.evomaster.client.java.sql.dsl.SqlDsl.sql;
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
import org.zalando.catwatch.backend.repo.StatisticsRepositoryImpl;
import org.zalando.catwatch.backend.model.Statistics;
import org.zalando.catwatch.backend.model.QStatistics;

import java.util.Date;
import java.text.SimpleDateFormat;

public class gpt4o_run01_StatisticsRepositoryImplTest {

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
    public void testConstructorCoverage() {
        // Instantiating StatisticsRepositoryImpl explicitly to cover the constructor
        StatisticsRepositoryImpl repository = new StatisticsRepositoryImpl();
        assertNotNull(repository);
    }
    
    @Test
    public void testGetLatestSnapshotDateBefore_withData() throws Exception {
        // Insert data into the test database for QStatistics usage
        controller.insertIntoDatabaseWithSql(
            sql(Arrays.asList(
                new InsertionDto("STATISTICS", Arrays.asList("id", "snapshotDate", "organizationName"), Arrays.asList(Arrays.asList("1"), Arrays.asList("2023-10-01"), Arrays.asList("Org1")))
            ))
        );

        // Call the method under test
        StatisticsRepositoryImpl repository = new StatisticsRepositoryImpl();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beforeDate = sdf.parse("2023-10-02");
        Optional<Date> result = repository.getLatestSnaphotDateBefore("Org1", beforeDate);

        // Verify the result
        assertTrue(result.isPresent());
        assertEquals(sdf.parse("2023-10-01"), result.get());
    }

    @Test
    public void testGetLatestSnapshotDateBefore_noMatch() throws Exception {
        // Insert data that won't match the query
        controller.insertIntoDatabaseWithSql(
            sql(Arrays.asList(
                new InsertionDto("STATISTICS", Arrays.asList("id", "snapshotDate", "organizationName"), Arrays.asList(Arrays.asList("2"), Arrays.asList("2023-05-01"), Arrays.asList("Org1")))
            ))
        );

        // Call the method under test
        StatisticsRepositoryImpl repository = new StatisticsRepositoryImpl();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date beforeDate = sdf.parse("2023-04-01");
        Optional<Date> result = repository.getLatestSnaphotDateBefore("Org1", beforeDate);

        // Verify the result
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetEarliestSnapshotDate_withData() throws Exception {
        // Insert data into the database
        controller.insertIntoDatabaseWithSql(
            sql(Arrays.asList(
                new InsertionDto("STATISTICS", Arrays.asList("id", "snapshotDate", "organizationName"), Arrays.asList(Arrays.asList("1"), Arrays.asList("2023-01-01"), Arrays.asList("Org2")))
            ))
        );

        // Call the method under test
        StatisticsRepositoryImpl repository = new StatisticsRepositoryImpl();
        Optional<Date> result = repository.getEarliestSnaphotDate("Org2");

        // Verify the result
        assertTrue(result.isPresent());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01"), result.get());
    }

    @Test
    public void testGetEarliestSnapshotDate_noMatch() {
        // Call method under test with no matching data inserted
        StatisticsRepositoryImpl repository = new StatisticsRepositoryImpl();
        Optional<Date> result = repository.getEarliestSnaphotDate("NonExistentOrg");

        // Verify the result
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetEarliestSnapshotDate_emptyResult() throws Exception {
        // Call the method without insertion
        StatisticsRepositoryImpl repository = new StatisticsRepositoryImpl();
        Optional<Date> result = repository.getEarliestSnaphotDate("Org3");

        // Method should return empty if table has no data
        assertFalse(result.isPresent());
    }
}
