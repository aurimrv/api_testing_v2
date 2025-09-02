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

import org.zalando.catwatch.backend.model.Project;
import org.zalando.catwatch.backend.service.comparator.ProjectStarComparator;

public class sonnet35_run01_ProjectStarComparatorTest {

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
    public void testCompareEqualStars() {
        ProjectStarComparator comparator = new ProjectStarComparator();
        Project p1 = new Project();
        Project p2 = new Project();
        p1.setStarsCount(100);
        p2.setStarsCount(100);
        
        int result = comparator.compare(p1, p2);
        
        assertEquals(0, result);
    }

    @Test
    public void testCompareFirstProjectMoreStars() {
        ProjectStarComparator comparator = new ProjectStarComparator();
        Project p1 = new Project();
        Project p2 = new Project();
        p1.setStarsCount(200);
        p2.setStarsCount(100);
        
        int result = comparator.compare(p1, p2);
        
        assertTrue(result > 0);
    }

    @Test
    public void testCompareSecondProjectMoreStars() {
        ProjectStarComparator comparator = new ProjectStarComparator();
        Project p1 = new Project();
        Project p2 = new Project();
        p1.setStarsCount(100);
        p2.setStarsCount(200);
        
        int result = comparator.compare(p1, p2);
        
        assertTrue(result < 0);
    }

    @Test
    public void testCompareWithZeroStars() {
        ProjectStarComparator comparator = new ProjectStarComparator();
        Project p1 = new Project();
        Project p2 = new Project();
        p1.setStarsCount(0);
        p2.setStarsCount(0);
        
        int result = comparator.compare(p1, p2);
        
        assertEquals(0, result);
    }

    @Test
    public void testCompareWithNegativeStars() {
        ProjectStarComparator comparator = new ProjectStarComparator();
        Project p1 = new Project();
        Project p2 = new Project();
        p1.setStarsCount(-10);
        p2.setStarsCount(-20);
        
        int result = comparator.compare(p1, p2);
        
        assertTrue(result > 0);
    }

    @Test
    public void testConstructor() {
        ProjectStarComparator comparator = new ProjectStarComparator();
        assertNotNull(comparator);
    }
}
