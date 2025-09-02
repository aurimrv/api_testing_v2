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

import org.zalando.catwatch.backend.repo.StatisticsRepositoryImpl;
import java.util.Date;
import java.util.Optional;

public class sonnet35_run01_StatisticsRepositoryImplTest {

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
    public void testGetLatestSnaphotDateBefore() {
        StatisticsRepositoryImpl repo = new StatisticsRepositoryImpl();
        
        // Test case 1: Normal case
        Date snapshot = new Date();
        Optional<Date> result = repo.getLatestSnaphotDateBefore("testOrg", snapshot);
        assertFalse(result.isPresent());
        
        // Test case 2: Null organization
        result = repo.getLatestSnaphotDateBefore(null, snapshot);
        assertFalse(result.isPresent());
        
        // Test case 3: Null snapshot date
        result = repo.getLatestSnaphotDateBefore("testOrg", null);
        assertFalse(result.isPresent());
        
        // Test case 4: Both null parameters
        result = repo.getLatestSnaphotDateBefore(null, null);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetEarliestSnaphotDate() {
        StatisticsRepositoryImpl repo = new StatisticsRepositoryImpl();
        
        // Test case 1: Normal case
        Optional<Date> result = repo.getEarliestSnaphotDate("testOrg");
        assertFalse(result.isPresent());
        
        // Test case 2: Null organization
        result = repo.getEarliestSnaphotDate(null);
        assertFalse(result.isPresent());
    }

    @Test
    public void testConstructor() {
        StatisticsRepositoryImpl repo = new StatisticsRepositoryImpl();
        assertNotNull(repo);
    }
}
