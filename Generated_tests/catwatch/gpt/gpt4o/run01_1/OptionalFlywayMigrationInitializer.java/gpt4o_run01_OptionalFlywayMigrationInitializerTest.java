
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

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.flywaydb.core.Flyway;

public class gpt4o_run01_OptionalFlywayMigrationInitializerTest {

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
    public void testConstructor() {
        // Ensure the constructor is covered
        Flyway flywayMock = org.mockito.Mockito.mock(Flyway.class);
        FlywayMigrationStrategy strategyMock = org.mockito.Mockito.mock(FlywayMigrationStrategy.class);

        FlywayMigrationInitializer initializer =
            new FlywayMigrationInitializer(flywayMock, strategyMock);

        assertNotNull(initializer);
    }

    @Test
    public void testAfterPropertiesSet_WhenDatabaseAvailable() throws Exception {
        // Mock JdbcTemplate
        org.springframework.jdbc.core.JdbcTemplate jdbcTemplateMock = org.mockito.Mockito.mock(org.springframework.jdbc.core.JdbcTemplate.class);

        // Mock Flyway and migration strategy
        Flyway flywayMock = org.mockito.Mockito.mock(Flyway.class);
        FlywayMigrationStrategy strategyMock = org.mockito.Mockito.mock(FlywayMigrationStrategy.class);

        // Instantiate class under test
        FlywayMigrationInitializer initializer =
            new FlywayMigrationInitializer(flywayMock, strategyMock);

        // Inject the mocked jdbcTemplate via reflection
        java.lang.reflect.Field flywayField = FlywayMigrationInitializer.class.getDeclaredField("flyway");
        flywayField.setAccessible(true);
        flywayField.set(initializer, flywayMock);

        // Mock the database availability and Flyway initialization
        org.mockito.Mockito.when(org.zalando.catwatch.backend.repo.util.DatabasePing.isDatabaseAvailable(jdbcTemplateMock)).thenReturn(true);

        // Call the method under test
        initializer.afterPropertiesSet();

        // Verify that the super implementation was called
        org.mockito.Mockito.verify(strategyMock, org.mockito.Mockito.times(1)).migrate(flywayMock);
    }

    @Test
    public void testAfterPropertiesSet_WhenDatabaseUnavailable() throws Exception {
        // Mock JdbcTemplate
        org.springframework.jdbc.core.JdbcTemplate jdbcTemplateMock = org.mockito.Mockito.mock(org.springframework.jdbc.core.JdbcTemplate.class);

        // Mock Flyway and migration strategy
        Flyway flywayMock = org.mockito.Mockito.mock(Flyway.class);
        FlywayMigrationStrategy strategyMock = org.mockito.Mockito.mock(FlywayMigrationStrategy.class);

        // Instantiate class under test
        FlywayMigrationInitializer initializer =
            new FlywayMigrationInitializer(flywayMock, strategyMock);

        // Inject the mocked jdbcTemplate via reflection
        java.lang.reflect.Field flywayField = FlywayMigrationInitializer.class.getDeclaredField("flyway");
        flywayField.setAccessible(true);
        flywayField.set(initializer, flywayMock);

        // Mock the database availability
        org.mockito.Mockito.when(org.zalando.catwatch.backend.repo.util.DatabasePing.isDatabaseAvailable(jdbcTemplateMock)).thenReturn(false);

        // Call the method under test
        initializer.afterPropertiesSet();

        // Verify that the super implementation was NOT called
        org.mockito.Mockito.verify(strategyMock, org.mockito.Mockito.never()).migrate(flywayMock);
    }
}
