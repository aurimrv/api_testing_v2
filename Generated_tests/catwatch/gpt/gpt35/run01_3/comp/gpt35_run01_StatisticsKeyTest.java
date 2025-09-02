
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

public class gpt35_run01_StatisticsKeyTest {

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
    public void testQStatisticsKeyClinit() {
        // Test QStatisticsKey <clinit> method to cover all lines
        // No specific actions are required as it's static initialization
        new org.zalando.catwatch.backend.model.QStatisticsKey("");

    }

    @Test
    public void testQStatisticsKeyInit() {
        // Test QStatisticsKey <init> method to cover all lines
        // As this is a Lombok generated class, instantiation will not be easily possible

        // Manually invoking the constructors to ensure they are executed
        new org.zalando.catwatch.backend.model.QStatisticsKey("");

        // This <init> method doesn't have any specific behavior, so no further tests are needed
    }

    @Test
    public void testStatisticsKeyInit() {
        // Test StatisticsKey <init> method to cover all lines
        // Constructor lines: 18, 27, 28, 29, 30

        org.zalando.catwatch.backend.model.StatisticsKey statisticsKey = new org.zalando.catwatch.backend.model.StatisticsKey(1L, new java.util.Date());

        assertNotNull(statisticsKey);
        assertEquals(1L, statisticsKey.getId());
        assertNotNull(statisticsKey.getSnapshotDate());
    }
}
