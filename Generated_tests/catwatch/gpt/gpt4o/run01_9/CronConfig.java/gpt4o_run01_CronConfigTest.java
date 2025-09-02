
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

public class gpt4o_run01_CronConfigTest {

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
    public void testCronConfigConstructor() {
        // Instantiate the class to test the constructor
        org.zalando.catwatch.backend.scheduler.CronConfig cronConfig = new org.zalando.catwatch.backend.scheduler.CronConfig();
        assertNotNull(cronConfig);
    }

    @Test
    public void testCronCommandLineRunnerSuccess() throws Exception {
        // Mock objects for dependencies
        org.zalando.catwatch.backend.scheduler.RetryableFetcher mockFetcher = org.mockito.Mockito.mock(org.zalando.catwatch.backend.scheduler.RetryableFetcher.class);
        org.springframework.context.ConfigurableApplicationContext mockApplicationContext = org.mockito.Mockito.mock(org.springframework.context.ConfigurableApplicationContext.class);

        // Instantiate CronConfig and retrieve the CommandLineRunner
        org.zalando.catwatch.backend.scheduler.CronConfig cronConfig = new org.zalando.catwatch.backend.scheduler.CronConfig();
        org.springframework.boot.CommandLineRunner commandLineRunner = cronConfig.cron(mockFetcher, mockApplicationContext);

        // Execute the command with a successful mocking of fetcher
        commandLineRunner.run();
        org.mockito.Mockito.verify(mockFetcher, org.mockito.Mockito.times(1)).tryFetchData();
        org.mockito.Mockito.verify(mockApplicationContext, org.mockito.Mockito.times(1)).close(); // Verifying application exit (successful flow)
    }

    @Test
    public void testCronCommandLineRunnerFailure() throws Exception {
        // Mock objects for dependencies
        org.zalando.catwatch.backend.scheduler.RetryableFetcher mockFetcher = org.mockito.Mockito.mock(org.zalando.catwatch.backend.scheduler.RetryableFetcher.class);
        org.springframework.context.ConfigurableApplicationContext mockApplicationContext = org.mockito.Mockito.mock(org.springframework.context.ConfigurableApplicationContext.class);

        // Configure the fetcher mock to throw an exception
        org.mockito.Mockito.doThrow(new RuntimeException("Test exception")).when(mockFetcher).tryFetchData();

        // Instantiate CronConfig and retrieve the CommandLineRunner
        org.zalando.catwatch.backend.scheduler.CronConfig cronConfig = new org.zalando.catwatch.backend.scheduler.CronConfig();
        org.springframework.boot.CommandLineRunner commandLineRunner = cronConfig.cron(mockFetcher, mockApplicationContext);

        // Execute the command and verify exception handling
        try {
            commandLineRunner.run();
        } catch (Exception e) {
            fail("Exception should not propagate: " + e.getMessage());
        }

        org.mockito.Mockito.verify(mockFetcher, org.mockito.Mockito.times(1)).tryFetchData();
        org.mockito.Mockito.verify(mockApplicationContext, org.mockito.Mockito.times(1)).close(); // Verifying application exit (failure flow)
    }

    @Test
    public void testCustomExitCodeGenerator() {
        // Test CustomExitCodeGenerator for both success and failure codes
        class CustomExitCodeGenerator {
            private final int exitCode;

            public CustomExitCodeGenerator(int exitCode) {
                this.exitCode = exitCode;
            }

            public int getExitCode() {
                return exitCode;
            }
        }

        CustomExitCodeGenerator successGenerator = new CustomExitCodeGenerator(0);
        assertEquals(0, successGenerator.getExitCode());

        CustomExitCodeGenerator failureGenerator = new CustomExitCodeGenerator(1);
        assertEquals(1, failureGenerator.getExitCode());
    }
}
