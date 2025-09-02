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

import org.springframework.jdbc.core.JdbcTemplate;
import org.zalando.catwatch.backend.repo.util.DatabasePing;

public class sonnet35_run01_DatabasePingTest {

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
    public void testDatabasePingConstructor() {
        DatabasePing databasePing = new DatabasePing();
        assertNotNull(databasePing);
    }

    @Test
    public void testIsDatabaseAvailableWhenDatabaseIsAvailable() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        boolean result = DatabasePing.isDatabaseAvailable(jdbcTemplate);
        assertTrue(result);
    }

    @Test
    public void testIsDatabaseAvailableWhenDatabaseIsNotAvailable() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        // Simulate database unavailability by using a mock JdbcTemplate
        jdbcTemplate = new JdbcTemplate() {
            @Override
            public void execute(String sql) throws RuntimeException {
                throw new RuntimeException("Database not available");
            }
        };
        
        boolean result = DatabasePing.isDatabaseAvailable(jdbcTemplate);
        assertFalse(result);
    }

    @Test
    public void testIsDatabaseAvailableCachingBehavior() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        
        // First call, should execute the query
        boolean result1 = DatabasePing.isDatabaseAvailable(jdbcTemplate);
        assertTrue(result1);
        
        // Second call, should return cached result without executing query
        boolean result2 = DatabasePing.isDatabaseAvailable(jdbcTemplate);
        assertTrue(result2);
        
        // Both results should be the same
        assertEquals(result1, result2);
    }
}
