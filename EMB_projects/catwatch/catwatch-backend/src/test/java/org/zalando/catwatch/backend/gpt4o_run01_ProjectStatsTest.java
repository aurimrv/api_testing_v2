
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

import org.zalando.catwatch.backend.util.ProjectStats;
import org.zalando.catwatch.backend.model.Project;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class gpt4o_run01_ProjectStatsTest {

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
    public void testProjectStatsConstructorAndLambda() {
        // Test valid input
        List<Project> projects = new ArrayList<>();
        Project project1 = new Project();
        project1.setName("Project1");
        project1.setOrganizationName("Org1");
        project1.setUrl("http://example.com");
        project1.setDescription("Test Project");
        project1.setPrimaryLanguage("Java");
        project1.setCommitsCount(10);
        project1.setForksCount(2);
        project1.setContributorsCount(5);
        project1.setScore(100);
        project1.setSnapshotDate(new Date());

        Project project2 = new Project();
        project2.setName("Project1");
        project2.setOrganizationName("Org1");
        project2.setUrl("http://example.com");
        project2.setDescription("Test Project");
        project2.setPrimaryLanguage("Java");
        project2.setCommitsCount(20);
        project2.setForksCount(5);
        project2.setContributorsCount(10);
        project2.setScore(90);
        project2.setSnapshotDate(new Date(System.currentTimeMillis() + 1000));

        projects.add(project1);
        projects.add(project2);

        ProjectStats stats = new ProjectStats(projects);

        assertEquals("Project1", stats.getName());
        assertEquals("Org1", stats.getOrganizationName());
        assertEquals("http://example.com", stats.getUrl());
        assertEquals("Test Project", stats.getDescription());
        assertEquals("Java", stats.getPrimaryLanguage());
        assertEquals((Integer) 10, stats.getCommitCounts().get(0));
        assertEquals((Integer) 20, stats.getCommitCounts().get(1));
        assertEquals((Integer) 2, stats.getForkCounts().get(0));
        assertEquals((Integer) 5, stats.getForkCounts().get(1));
        assertEquals((Integer) 5, stats.getContributorsCounts().get(0));
        assertEquals((Integer) 10, stats.getContributorsCounts().get(1));
        assertEquals((Integer) 100, stats.getScores().get(0));
        assertEquals((Integer) 90, stats.getScores().get(1));
        assertEquals(2, stats.getSnapshotDates().size());

        // Test exception when projects have different names
        project2.setName("DifferentProject");
        projects.set(1, project2);

        try {
            new ProjectStats(projects);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertEquals("All projects in the list must refer to the same project!", ex.getMessage());
        }
    }

    @Test
    public void testProjectStatsEmptyProjectList() {
        // Test an empty project list
        try {
            new ProjectStats(new ArrayList<>());
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            assertNotNull(ex);
        }
    }

    @Test
    public void testProjectStatsSingleProject() {
        // Test with a single project
        List<Project> projects = new LinkedList<>();
        Project project = new Project();
        project.setName("SingleProject");
        project.setOrganizationName("SingleOrg");
        project.setUrl("http://single.example.com");
        project.setDescription("A single test project");
        project.setPrimaryLanguage("Python");
        project.setCommitsCount(15);
        project.setForksCount(3);
        project.setContributorsCount(8);
        project.setScore(80);
        project.setSnapshotDate(new Date());

        projects.add(project);

        ProjectStats stats = new ProjectStats(projects);

        assertEquals("SingleProject", stats.getName());
        assertEquals("SingleOrg", stats.getOrganizationName());
        assertEquals("http://single.example.com", stats.getUrl());
        assertEquals("A single test project", stats.getDescription());
        assertEquals("Python", stats.getPrimaryLanguage());
        assertEquals(1, stats.getCommitCounts().size());
        assertEquals((Integer) 15, stats.getCommitCounts().get(0));
        assertEquals((Integer) 3, stats.getForkCounts().get(0));
        assertEquals((Integer) 8, stats.getContributorsCounts().get(0));
        assertEquals((Integer) 80, stats.getScores().get(0));
        assertEquals(1, stats.getSnapshotDates().size());
    }
}
