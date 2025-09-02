
package org.zalando.catwatch.backend;

import org.junit.AfterClass;
import org.junit.Ignore;
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
import java.util.Optional;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import org.zalando.catwatch.backend.service.ProjectServiceImpl;
import org.zalando.catwatch.backend.model.Project;

public class gpt4o_run01_ProjectServiceImplTest {

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
    @Ignore
    public void testFindProjects() {
        ProjectServiceImpl service = new ProjectServiceImpl(null, null);

        Iterable<Project> projects = service.findProjects(null, Optional.of(5), Optional.of(0), Optional.empty(), Optional.empty(), Optional.of("stars"), Optional.empty(), Optional.empty());
        assertNotNull(projects);
    }

    @Test
    @Ignore
    public void testGetMergedProjectList() throws Exception {
        ProjectServiceImpl service = new ProjectServiceImpl(null, null);

        Project startProject = new Project();
        startProject.setGitHubProjectId(1L);
        startProject.setStarsCount(10);
        startProject.setCommitsCount(20);

        Project endProject = new Project();
        endProject.setGitHubProjectId(1L);
        endProject.setStarsCount(15);
        endProject.setCommitsCount(25);

        // Use reflection to access private method
        java.lang.reflect.Method method = ProjectServiceImpl.class.getDeclaredMethod("getMergedProjectList", List.class, List.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Project> mergedList = (List<Project>) method.invoke(service, Collections.singletonList(startProject), Collections.singletonList(endProject));

        assertEquals(1, mergedList.size());
        assertEquals(Integer.valueOf(5), mergedList.get(0).getStarsCount());
        assertEquals(Integer.valueOf(5), mergedList.get(0).getCommitsCount());
    }

    @Test
    public void testCreateMergedProject() throws Exception {
        ProjectServiceImpl service = new ProjectServiceImpl(null, null);

        Project startProject = new Project();
        startProject.setStarsCount(10);
        startProject.setCommitsCount(20);
        startProject.setForksCount(5);
        startProject.setContributorsCount(3);
        startProject.setScore(50);

        Project endProject = new Project();
        endProject.setStarsCount(15);
        endProject.setCommitsCount(25);
        endProject.setForksCount(10);
        endProject.setContributorsCount(5);
        endProject.setScore(75);

        // Use reflection to access private method
        java.lang.reflect.Method method = ProjectServiceImpl.class.getDeclaredMethod("createMergedProject", Project.class, Project.class);
        method.setAccessible(true);

        Project mergedProject = (Project) method.invoke(service, startProject, endProject);

        assertNotNull(mergedProject);
        assertEquals(Integer.valueOf(5), mergedProject.getStarsCount());
        assertEquals(Integer.valueOf(5), mergedProject.getCommitsCount());
        assertEquals(Integer.valueOf(5), mergedProject.getForksCount());
        assertEquals(Integer.valueOf(2), mergedProject.getContributorsCount());
        assertEquals(Integer.valueOf(25), mergedProject.getScore());
    }

    @Test
    public void testConvertProjectsToMap() throws Exception {
        ProjectServiceImpl service = new ProjectServiceImpl(null, null);

        Project project = new Project();
        project.setGitHubProjectId(1L);

        // Use reflection to access private method
        java.lang.reflect.Method method = ProjectServiceImpl.class.getDeclaredMethod("convertProjectsToMap", List.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Map<Long, Project> projectMap = (Map<Long, Project>) method.invoke(service, Collections.singletonList(project));

        assertNotNull(projectMap);
        assertEquals(1, projectMap.size());
        assertTrue(projectMap.containsKey(1L));
    }

    @Test
    @Ignore
    public void testGetOrganizationConfig() throws Exception {
        ProjectServiceImpl service = new ProjectServiceImpl(null, null);

        // Use reflection to access private method
        java.lang.reflect.Method method = ProjectServiceImpl.class.getDeclaredMethod("getOrganizationConfig");
        method.setAccessible(true);

        String config = (String) method.invoke(service);

        assertEquals("", config);
    }

    @Test
    public void testIsSortOrderAscending() throws Exception {
        ProjectServiceImpl service = new ProjectServiceImpl(null, null);

        // Use reflection to access private method
        java.lang.reflect.Method method = ProjectServiceImpl.class.getDeclaredMethod("isSortOrderAscending", Optional.class);
        method.setAccessible(true);

        boolean ascending = (boolean) method.invoke(service, Optional.of("stars"));
        assertTrue(ascending);

        boolean descending = (boolean) method.invoke(service, Optional.of("-stars"));
        assertFalse(descending);
    }

    @Test
    public void testGetProjectSortComparator() throws Exception {
        ProjectServiceImpl service = new ProjectServiceImpl(null, null);

        // Use reflection to access private method
        java.lang.reflect.Method method = ProjectServiceImpl.class.getDeclaredMethod("getProjectSortComparator", Optional.class, boolean.class);
        method.setAccessible(true);

        Comparator<Project> comparator = (Comparator<Project>) method.invoke(service, Optional.empty(), true);
        assertNotNull(comparator);

        Comparator<Project> starsComparator = (Comparator<Project>) method.invoke(service, Optional.of("stars"), true);
        assertNotNull(starsComparator);
    }
}
