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
import org.zalando.catwatch.backend.model.Language;
import org.zalando.catwatch.backend.model.Project;
import org.zalando.catwatch.backend.repo.ProjectRepository;
import org.zalando.catwatch.backend.service.LanguageService;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Comparator;
import org.mockito.Mockito;

public class sonnet35_run01_LanguageServiceTest {

    private static final SutHandler controller = new em.embedded.org.zalando.EmbeddedEvoMasterController();
    private static String baseUrlOfSut;

    private ProjectRepository mockRepository;
    private LanguageService languageService;

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
        mockRepository = Mockito.mock(ProjectRepository.class);
        languageService = new LanguageService(mockRepository);
    }

    @Test
    public void testGetMainLanguagesWithEmptyProjects() {
        Mockito.when(mockRepository.findProjects(Mockito.anyString(), Mockito.any(), Mockito.any()))
               .thenReturn(new ArrayList<>());

        List<Language> result = languageService.getMainLanguages("org1,org2", Comparator.comparing(Language::getProjectsCount).reversed(), Optional.empty());

        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetMainLanguagesWithSingleProject() {
        Project project = new Project();
        project.setPrimaryLanguage("Java");
        project.setName("Project1");

        Mockito.when(mockRepository.findProjects(Mockito.anyString(), Mockito.any(), Mockito.any()))
               .thenReturn(Arrays.asList(project));

        List<Language> result = languageService.getMainLanguages("org1", Comparator.comparing(Language::getProjectsCount).reversed(), Optional.empty());

        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getName());
        assertEquals(1L, (long) result.get(0).getProjectsCount());
        assertEquals(100L, (long) result.get(0).getPercentage());
    }

    @Test
    public void testGetMainLanguagesWithMultipleProjects() {
        Project project1 = new Project();
        project1.setPrimaryLanguage("Java");
        project1.setName("Project1");

        Project project2 = new Project();
        project2.setPrimaryLanguage("Python");
        project2.setName("Project2");

        Project project3 = new Project();
        project3.setPrimaryLanguage("Java");
        project3.setName("Project3");

        Mockito.when(mockRepository.findProjects(Mockito.anyString(), Mockito.any(), Mockito.any()))
               .thenReturn(Arrays.asList(project1, project2, project3));

        List<Language> result = languageService.getMainLanguages("org1,org2", Comparator.comparing(Language::getProjectsCount).reversed(), Optional.empty());

        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).getName());
        assertEquals(2L, (long) result.get(0).getProjectsCount());
        assertEquals(67L, (long) result.get(0).getPercentage());
        assertEquals("Python", result.get(1).getName());
        assertEquals(1L, (long) result.get(1).getProjectsCount());
        assertEquals(33L, (long) result.get(1).getPercentage());
    }

    @Test
    public void testGetMainLanguagesWithLanguageFilter() {
        Project project1 = new Project();
        project1.setPrimaryLanguage("Java");
        project1.setName("Project1");

        Project project2 = new Project();
        project2.setPrimaryLanguage("Python");
        project2.setName("Project2");

        Mockito.when(mockRepository.findProjects(Mockito.anyString(), Mockito.any(), Mockito.eq(Optional.of("Java"))))
               .thenReturn(Arrays.asList(project1));

        List<Language> result = languageService.getMainLanguages("org1", Comparator.comparing(Language::getProjectsCount).reversed(), Optional.of("Java"));

        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getName());
        assertEquals(1L, (long) result.get(0).getProjectsCount());
        assertEquals(100L, (long) result.get(0).getPercentage());
    }

    @Test
    public void testGetMainLanguagesWithNullPrimaryLanguage() {
        Project project1 = new Project();
        project1.setPrimaryLanguage(null);
        project1.setName("Project1");

        Project project2 = new Project();
        project2.setPrimaryLanguage("Python");
        project2.setName("Project2");

        Mockito.when(mockRepository.findProjects(Mockito.anyString(), Mockito.any(), Mockito.any()))
               .thenReturn(Arrays.asList(project1, project2));

        List<Language> result = languageService.getMainLanguages("org1", Comparator.comparing(Language::getProjectsCount).reversed(), Optional.empty());

        assertEquals(1, result.size());
        assertEquals("Python", result.get(0).getName());
        assertEquals(1L, (long) result.get(0).getProjectsCount());
        assertEquals(100L, (long) result.get(0).getPercentage());
    }

    @Test
    public void testGetMainLanguagesWithEmptyPrimaryLanguage() {
        Project project1 = new Project();
        project1.setPrimaryLanguage("");
        project1.setName("Project1");

        Project project2 = new Project();
        project2.setPrimaryLanguage("Python");
        project2.setName("Project2");

        Mockito.when(mockRepository.findProjects(Mockito.anyString(), Mockito.any(), Mockito.any()))
               .thenReturn(Arrays.asList(project1, project2));

        List<Language> result = languageService.getMainLanguages("org1", Comparator.comparing(Language::getProjectsCount).reversed(), Optional.empty());

        assertEquals(1, result.size());
        assertEquals("Python", result.get(0).getName());
        assertEquals(1L, (long) result.get(0).getProjectsCount());
        assertEquals(100L, (long) result.get(0).getPercentage());
    }

    @Test
    public void testGetMainLanguagesWithCustomComparator() {
        Project project1 = new Project();
        project1.setPrimaryLanguage("Java");
        project1.setName("Project1");

        Project project2 = new Project();
        project2.setPrimaryLanguage("Python");
        project2.setName("Project2");

        Mockito.when(mockRepository.findProjects(Mockito.anyString(), Mockito.any(), Mockito.any()))
               .thenReturn(Arrays.asList(project1, project2));

        List<Language> result = languageService.getMainLanguages("org1", Comparator.comparing(Language::getName), Optional.empty());

        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).getName());
        assertEquals("Python", result.get(1).getName());
    }
}
