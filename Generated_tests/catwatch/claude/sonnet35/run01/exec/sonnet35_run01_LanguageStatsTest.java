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
import org.zalando.catwatch.backend.util.LanguageStats;
import org.zalando.catwatch.backend.model.Project;
import java.util.ArrayList;
import java.util.Date;

public class sonnet35_run01_LanguageStatsTest {

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
        String languageName = "Java";
        List<Integer> projectCounts = Arrays.asList(1, 2, 3);
        List<Date> snapshotDates = Arrays.asList(new Date(), new Date());

        LanguageStats stats = new LanguageStats(languageName, projectCounts, snapshotDates);

        assertEquals(languageName, stats.getLanguageName());
        assertEquals(projectCounts, stats.getProjectCounts());
        assertEquals(snapshotDates, stats.getSnapshotDates());
    }

    @Test
    public void testConstructorWithNullLanguageName() {
        LanguageStats stats = new LanguageStats(null, new ArrayList<>(), new ArrayList<>());
        assertEquals(LanguageStats.UNKNOWN, stats.getLanguageName());
    }

    @Test
    public void testBuildStats() {
        List<Project> projects = new ArrayList<>();
        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 86400000); // Next day

        Project p1 = new Project();
        p1.setPrimaryLanguage("Java");
        p1.setSnapshotDate(date1);

        Project p2 = new Project();
        p2.setPrimaryLanguage("Python");
        p2.setSnapshotDate(date1);

        Project p3 = new Project();
        p3.setPrimaryLanguage("Java");
        p3.setSnapshotDate(date2);

        projects.add(p1);
        projects.add(p2);
        projects.add(p3);

        List<LanguageStats> result = LanguageStats.buildStats(projects);

        assertEquals(2, result.size());
        
        LanguageStats javaStats = result.stream().filter(s -> s.getLanguageName().equals("Java")).findFirst().orElse(null);
        assertNotNull(javaStats);
        assertEquals(Arrays.asList(1, 1), javaStats.getProjectCounts());
        assertEquals(Arrays.asList(date1, date2), javaStats.getSnapshotDates());

        LanguageStats pythonStats = result.stream().filter(s -> s.getLanguageName().equals("Python")).findFirst().orElse(null);
        assertNotNull(pythonStats);
        assertEquals(Arrays.asList(1, 0), pythonStats.getProjectCounts());
        assertEquals(Arrays.asList(date1, date2), pythonStats.getSnapshotDates());
    }

    @Test
    public void testFilterUniqueSnapshots() {
        List<Project> projects = new ArrayList<>();
        Date date = new Date();

        Project p1 = new Project();
        p1.setPrimaryLanguage("Java");
        p1.setName("Project1");
        p1.setOrganizationName("Org1");
        p1.setSnapshotDate(date);

        Project p2 = new Project();
        p2.setPrimaryLanguage("Java");
        p2.setName("Project1");
        p2.setOrganizationName("Org1");
        p2.setSnapshotDate(date);

        Project p3 = new Project();
        p3.setPrimaryLanguage("Python");
        p3.setName("Project2");
        p3.setOrganizationName("Org1");
        p3.setSnapshotDate(date);

        projects.add(p1);
        projects.add(p2);
        projects.add(p3);

        List<Project> result = LanguageStats.filterUniqueSnapshots(projects);

        assertEquals(2, result.size());
        assertTrue(result.contains(p1));
        assertTrue(result.contains(p3));
    }

    @Test
    public void testGetters() {
        String languageName = "Java";
        List<Integer> projectCounts = Arrays.asList(1, 2, 3);
        List<Date> snapshotDates = Arrays.asList(new Date(), new Date());

        LanguageStats stats = new LanguageStats(languageName, projectCounts, snapshotDates);

        assertEquals(languageName, stats.getLanguageName());
        assertEquals(projectCounts, stats.getProjectCounts());
        assertEquals(snapshotDates, stats.getSnapshotDates());
    }
}
