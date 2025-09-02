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
import java.util.Date;
import java.util.ArrayList;

public class sonnet35_run01_ProjectTest {

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
    public void testGetSnapshotDate() {
        Project project = new Project();
        Date date = new Date();
        project.setSnapshotDate(date);
        assertEquals(date, project.getSnapshotDate());
    }

    @Test
    public void testGetName() {
        Project project = new Project();
        project.setName("TestProject");
        assertEquals("TestProject", project.getName());
    }

    @Test
    public void testGetTitle() {
        Project project = new Project();
        project.setTitle("Test Title");
        assertEquals("Test Title", project.getTitle());
    }

    @Test
    public void testGetImage() {
        Project project = new Project();
        project.setImage("http://example.com/image.jpg");
        assertEquals("http://example.com/image.jpg", project.getImage());
    }

    @Test
    public void testGetOrganizationName() {
        Project project = new Project();
        project.setOrganizationName("TestOrg");
        assertEquals("TestOrg", project.getOrganizationName());
    }

    @Test
    public void testGetUrl() {
        Project project = new Project();
        project.setUrl("http://example.com/project");
        assertEquals("http://example.com/project", project.getUrl());
    }

    @Test
    public void testGetDescription() {
        Project project = new Project();
        project.setDescription("Test Description");
        assertEquals("Test Description", project.getDescription());
    }

    @Test
    public void testGetStarsCount() {
        Project project = new Project();
        project.setStarsCount(100);
        assertEquals(Integer.valueOf(100), project.getStarsCount());
    }

    @Test
    public void testGetCommitsCount() {
        Project project = new Project();
        project.setCommitsCount(50);
        assertEquals(Integer.valueOf(50), project.getCommitsCount());
    }

    @Test
    public void testGetForksCount() {
        Project project = new Project();
        project.setForksCount(25);
        assertEquals(Integer.valueOf(25), project.getForksCount());
    }

    @Test
    public void testGetContributorsCount() {
        Project project = new Project();
        project.setContributorsCount(10);
        assertEquals(Integer.valueOf(10), project.getContributorsCount());
    }

    @Test
    public void testGetExternalContributorsCount() {
        Project project = new Project();
        project.setExternalContributorsCount(5);
        assertEquals(Integer.valueOf(5), project.getExternalContributorsCount());
    }

    @Test
    public void testGetScore() {
        Project project = new Project();
        project.setScore(75);
        assertEquals(Integer.valueOf(75), project.getScore());
    }

    @Test
    public void testGetLastPushed() {
        Project project = new Project();
        project.setLastPushed("2023-05-01T12:00:00Z");
        assertEquals("2023-05-01T12:00:00Z", project.getLastPushed());
    }

    @Test
    public void testGetPrimaryLanguage() {
        Project project = new Project();
        project.setPrimaryLanguage("Java");
        assertEquals("Java", project.getPrimaryLanguage());
    }

    @Test
    public void testGetMaintainers() {
        Project project = new Project();
        List<String> maintainers = new ArrayList<>(Arrays.asList("Alice", "Bob"));
        project.setMaintainers(maintainers);
        assertEquals(maintainers, project.getMaintainers());
    }

    @Test
    public void testToString() {
        Project project = new Project();
        project.setName("TestProject");
        project.setOrganizationName("TestOrg");
        project.setDescription("Test Description");
        project.setStarsCount(100);
        String toString = project.toString();
        assertTrue(toString.contains("name=TestProject"));
        assertTrue(toString.contains("organizationName=TestOrg"));
        assertTrue(toString.contains("description=Test Description"));
        assertTrue(toString.contains("starsCount=100"));
    }

    @Test
    public void testConstructor() {
        Project project = new Project();
        assertNotNull(project);
    }
}
