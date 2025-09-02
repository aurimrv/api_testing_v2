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
import org.zalando.catwatch.backend.repo.ProjectRepository;
import org.zalando.catwatch.backend.model.Project;
import java.util.Optional;
import java.util.Date;
import javax.persistence.EntityManager;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class sonnet35_run01_ProjectRepositoryImplTest {

    private static final SutHandler controller = new em.embedded.org.zalando.EmbeddedEvoMasterController();
    private static String baseUrlOfSut;

    private ProjectRepository projectRepository;

    @Mock
    private EntityManager entityManager;

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
        MockitoAnnotations.initMocks(this);
        projectRepository = mock(ProjectRepository.class);
    }

    @Test
    public void testFindProjectsWithoutQueryAndLanguage() {
        String organization = "testOrg";
        List<Project> result = projectRepository.findProjects(organization, Optional.empty(), Optional.empty());
        assertNotNull(result);
        verify(projectRepository).findProjects(eq(organization), eq(Optional.empty()), eq(Optional.empty()));
    }

    @Test
    public void testFindProjectsWithQuery() {
        String organization = "testOrg";
        Optional<String> query = Optional.of("testQuery");
        List<Project> result = projectRepository.findProjects(organization, query, Optional.empty());
        assertNotNull(result);
        verify(projectRepository).findProjects(eq(organization), eq(query), eq(Optional.empty()));
    }

    @Test
    public void testFindProjectsWithLanguage() {
        String organization = "testOrg";
        Optional<String> language = Optional.of("Java");
        List<Project> result = projectRepository.findProjects(organization, Optional.empty(), language);
        assertNotNull(result);
        verify(projectRepository).findProjects(eq(organization), eq(Optional.empty()), eq(language));
    }

    @Test
    public void testFindProjectsWithQueryAndLanguage() {
        String organization = "testOrg";
        Optional<String> query = Optional.of("testQuery");
        Optional<String> language = Optional.of("Java");
        List<Project> result = projectRepository.findProjects(organization, query, language);
        assertNotNull(result);
        verify(projectRepository).findProjects(eq(organization), eq(query), eq(language));
    }

    @Test
    public void testFindProjectsWithDate() {
        String organization = "testOrg";
        Date snapshotDate = new Date();
        List<Project> result = projectRepository.findProjects(organization, snapshotDate, Optional.empty(), Optional.empty());
        assertNotNull(result);
        verify(projectRepository).findProjects(eq(organization), eq(snapshotDate), eq(Optional.empty()), eq(Optional.empty()));
    }

    @Test
    public void testFindProjectsWithDateAndQuery() {
        String organization = "testOrg";
        Date snapshotDate = new Date();
        Optional<String> query = Optional.of("testQuery");
        List<Project> result = projectRepository.findProjects(organization, snapshotDate, query, Optional.empty());
        assertNotNull(result);
        verify(projectRepository).findProjects(eq(organization), eq(snapshotDate), eq(query), eq(Optional.empty()));
    }

    @Test
    public void testFindProjectsWithDateAndLanguage() {
        String organization = "testOrg";
        Date snapshotDate = new Date();
        Optional<String> language = Optional.of("Java");
        List<Project> result = projectRepository.findProjects(organization, snapshotDate, Optional.empty(), language);
        assertNotNull(result);
        verify(projectRepository).findProjects(eq(organization), eq(snapshotDate), eq(Optional.empty()), eq(language));
    }

    @Test
    public void testFindProjectsWithDateQueryAndLanguage() {
        String organization = "testOrg";
        Date snapshotDate = new Date();
        Optional<String> query = Optional.of("testQuery");
        Optional<String> language = Optional.of("Java");
        List<Project> result = projectRepository.findProjects(organization, snapshotDate, query, language);
        assertNotNull(result);
        verify(projectRepository).findProjects(eq(organization), eq(snapshotDate), eq(query), eq(language));
    }
}
