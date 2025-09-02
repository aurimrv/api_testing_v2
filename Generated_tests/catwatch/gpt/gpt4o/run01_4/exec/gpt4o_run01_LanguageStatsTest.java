
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.zalando.catwatch.backend.model.Project; // Correct import for the Project class

public class gpt4o_run01_LanguageStatsTest {

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
    public void testBuildStatsWithValidProjects() {
        List<LanguageStats> stats = LanguageStats.buildStats(
            Arrays.asList(
                createProject("Java", "Project1", "Org1", new Date(1000)),
                createProject("Python", "Project2", "Org1", new Date(2000)),
                createProject("Java", "Project3", "Org1", new Date(1000))
            )
        );

        assertNotNull(stats);
        assertEquals(2, stats.size());

        for (LanguageStats stat : stats) {
            if ("Java".equals(stat.getLanguageName())) {
                assertEquals(Arrays.asList(2, 0), stat.getProjectCounts());
            } else if ("Python".equals(stat.getLanguageName())) {
                assertEquals(Arrays.asList(0, 1), stat.getProjectCounts());
            }
        }
    }

    @Test
    public void testBuildStatsWithEmptyProjects() {
        List<LanguageStats> stats = LanguageStats.buildStats(new ArrayList<>());
        assertNotNull(stats);
        assertTrue(stats.isEmpty());
    }

    @Test
    public void testBuildStatsWithNullLanguage() {
        List<LanguageStats> stats = LanguageStats.buildStats(
            Arrays.asList(
                createProject(null, "Project1", "Org1", new Date(1000)),
                createProject("Python", "Project2", "Org1", new Date(2000)),
                createProject(null, "Project3", "Org1", new Date(1000))
            )
        );

        assertNotNull(stats);
        assertEquals(2, stats.size());

        for (LanguageStats stat : stats) {
            if ("unknown".equals(stat.getLanguageName())) {
                assertEquals(Arrays.asList(2, 0), stat.getProjectCounts());
            } else if ("Python".equals(stat.getLanguageName())) {
                assertEquals(Arrays.asList(0, 1), stat.getProjectCounts());
            }
        }
    }

    @Test
    public void testFilterUniqueSnapshotsWithDuplicates() {
        List<Project> filtered = LanguageStats.filterUniqueSnapshots(
            Arrays.asList(
                createProject("Java", "Project1", "Org1", new Date(1000)),
                createProject("Java", "Project1", "Org1", new Date(1000)),
                createProject("Python", "Project2", "Org1", new Date(2000))
            )
        );

        assertNotNull(filtered);
        assertEquals(2, filtered.size());
    }

    @Test
    public void testFilterUniqueSnapshotsWithNoDuplicates() {
        List<Project> filtered = LanguageStats.filterUniqueSnapshots(
            Arrays.asList(
                createProject("Java", "Project1", "Org1", new Date(1000)),
                createProject("Python", "Project2", "Org1", new Date(2000))
            )
        );

        assertNotNull(filtered);
        assertEquals(2, filtered.size());
    }

    @Test
    public void testLanguageStatsConstructorWithNullLanguage() {
        LanguageStats stats = new LanguageStats(null, Arrays.asList(1, 2, 3), Arrays.asList(new Date(1000), new Date(2000)));
        assertNotNull(stats);
        assertEquals("unknown", stats.getLanguageName());
    }

    @Test
    public void testLanguageStatsConstructorWithNonNullLanguage() {
        LanguageStats stats = new LanguageStats("Java", Arrays.asList(1, 2, 3), Arrays.asList(new Date(1000), new Date(2000)));
        assertNotNull(stats);
        assertEquals("Java", stats.getLanguageName());
    }

    private Project createProject(String language, String name, String organization, Date snapshotDate) {
        Project project = new Project();
        project.setPrimaryLanguage(language);
        project.setName(name);
        project.setOrganizationName(organization);
        project.setSnapshotDate(snapshotDate);
        return project;
    }
}
