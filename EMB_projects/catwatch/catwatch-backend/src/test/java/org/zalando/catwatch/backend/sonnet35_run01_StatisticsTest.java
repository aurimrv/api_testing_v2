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
import org.zalando.catwatch.backend.model.Statistics;
import org.zalando.catwatch.backend.model.StatisticsKey;
import java.util.Date;

public class sonnet35_run01_StatisticsTest {

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
        Statistics stats = new Statistics();
        assertNotNull(stats);
        
        long id = 1L;
        Date date = new Date();
        Statistics statsWithParams = new Statistics(id, date);
        assertNotNull(statsWithParams);
        assertEquals(id, statsWithParams.getId());
        assertEquals(date, statsWithParams.getSnapshotDate());
    }

    @Test
    public void testGetId() {
        Statistics stats = new Statistics(123L, new Date());
        assertEquals(123L, stats.getId());
    }

    @Test
    public void testGetSnapshotDate() {
        Date date = new Date();
        Statistics stats = new Statistics(1L, date);
        assertEquals(date, stats.getSnapshotDate());
    }

    @Test
    public void testSetSnapshotDate() {
        Statistics stats = new Statistics();
        Date date = new Date();
        stats.setSnapshotDate(date);
        assertEquals(date, stats.getSnapshotDate());
    }

    @Test
    public void testToString() {
        Statistics stats = new Statistics(1L, new Date());
        stats.setPrivateProjectCount(10);
        stats.setPublicProjectCount(20);
        stats.setMembersCount(30);
        stats.setTeamsCount(40);
        stats.setAllContributorsCount(50);
        stats.setExternalContributorsCount(60);
        stats.setAllStarsCount(70);
        stats.setAllForksCount(80);
        stats.setAllSizeCount(90);
        stats.setProgramLanguagesCount(100);
        stats.setTagsCount(110);
        stats.setOrganizationName("TestOrg");

        String result = stats.toString();
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("privateProjectCount=10"));
        assertTrue(result.contains("publicProjectCount=20"));
        assertTrue(result.contains("membersCount=30"));
        assertTrue(result.contains("teamsCount=40"));
        assertTrue(result.contains("allContributorsCount=50"));
        assertTrue(result.contains("externalContributorsCount=60"));
        assertTrue(result.contains("allStarsCount=70"));
        assertTrue(result.contains("allForksCount=80"));
        assertTrue(result.contains("allSizeCount=90"));
        assertTrue(result.contains("programLanguagesCount=100"));
        assertTrue(result.contains("tagsCount=110"));
        assertTrue(result.contains("organizationName=TestOrg"));
    }

    @Test
    public void testGettersAndSetters() {
        Statistics stats = new Statistics();
        
        stats.setPrivateProjectCount(10);
        assertEquals(Integer.valueOf(10), stats.getPrivateProjectCount());
        
        stats.setPublicProjectCount(20);
        assertEquals(Integer.valueOf(20), stats.getPublicProjectCount());
        
        stats.setMembersCount(30);
        assertEquals(Integer.valueOf(30), stats.getMembersCount());
        
        stats.setTeamsCount(40);
        assertEquals(Integer.valueOf(40), stats.getTeamsCount());
        
        stats.setAllContributorsCount(50);
        assertEquals(Integer.valueOf(50), stats.getAllContributorsCount());
        
        stats.setExternalContributorsCount(60);
        assertEquals(Integer.valueOf(60), stats.getExternalContributorsCount());
        
        stats.setAllStarsCount(70);
        assertEquals(Integer.valueOf(70), stats.getAllStarsCount());
        
        stats.setAllForksCount(80);
        assertEquals(Integer.valueOf(80), stats.getAllForksCount());
        
        stats.setAllSizeCount(90);
        assertEquals(Integer.valueOf(90), stats.getAllSizeCount());
        
        stats.setProgramLanguagesCount(100);
        assertEquals(Integer.valueOf(100), stats.getProgramLanguagesCount());
        
        stats.setTagsCount(110);
        assertEquals(Integer.valueOf(110), stats.getTagsCount());
        
        stats.setOrganizationName("TestOrg");
        assertEquals("TestOrg", stats.getOrganizationName());
    }
}
