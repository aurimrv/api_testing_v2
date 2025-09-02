package org.zalando.catwatch.backend;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.zalando.catwatch.backend.model.Project;
import org.zalando.catwatch.backend.repo.ProjectRepository;
import org.zalando.catwatch.backend.service.ProjectServiceImpl;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class sonnet35_run01_ProjectServiceImplTest {

    private ProjectServiceImpl projectService;

    @Mock
    private ProjectRepository mockProjectRepository;

    @Mock
    private Environment mockEnvironment;

    @Before
    public void initTest() {
        MockitoAnnotations.initMocks(this);
        projectService = new ProjectServiceImpl(mockProjectRepository, mockEnvironment);
    }

    @Test
    public void testFindProjects() {
        String organizations = "org1,org2";
        Optional<Integer> limit = Optional.of(10);
        Optional<Integer> offset = Optional.of(0);
        Optional<Date> startDate = Optional.of(new Date());
        Optional<Date> endDate = Optional.of(new Date());
        Optional<String> sortBy = Optional.of("stars");
        Optional<String> query = Optional.of("test");
        Optional<String> language = Optional.of("Java");

        List<Project> mockProjects = new ArrayList<>();
        when(mockProjectRepository.findProjects(anyString(), any(Date.class), anyString(), anyString())).thenReturn(mockProjects);
        when(mockEnvironment.getProperty(anyString())).thenReturn("org1,org2");

        Iterable<Project> result = projectService.findProjects(organizations, limit, offset, startDate, endDate, sortBy, query, language);

        assertNotNull(result);
        verify(mockProjectRepository, times(4)).findProjects(anyString(), any(Date.class), anyString(), anyString());
    }

    @Test
    public void testGetMergedProjectList() throws Exception {
        List<Project> startProjects = new ArrayList<>();
        List<Project> endProjects = new ArrayList<>();
        Project project1 = new Project();
        project1.setGitHubProjectId(1L);
        project1.setStarsCount(10);
        startProjects.add(project1);
        Project project2 = new Project();
        project2.setGitHubProjectId(1L);
        project2.setStarsCount(15);
        endProjects.add(project2);

        List<Project> result = projectService.getMergedProjectList(startProjects, endProjects);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(5, result.get(0).getStarsCount());
    }

    @Test
    public void testCreateMergedProject() throws Exception {
        Project startProject = new Project();
        startProject.setStarsCount(10);
        startProject.setCommitsCount(100);
        startProject.setForksCount(5);
        startProject.setContributorsCount(20);
        startProject.setScore(50);

        Project endProject = new Project();
        endProject.setStarsCount(15);
        endProject.setCommitsCount(150);
        endProject.setForksCount(8);
        endProject.setContributorsCount(25);
        endProject.setScore(60);

        Project result = projectService.createMergedProject(startProject, endProject);

        assertNotNull(result);
        assertEquals(5, result.getStarsCount());
        assertEquals(50, result.getCommitsCount());
        assertEquals(3, result.getForksCount());
        assertEquals(5, result.getContributorsCount());
        assertEquals(10, result.getScore());
    }

    @Test
    public void testConvertProjectsToMap() throws Exception {
        List<Project> projects = new ArrayList<>();
        Project project1 = new Project();
        project1.setGitHubProjectId(1L);
        projects.add(project1);
        Project project2 = new Project();
        project2.setGitHubProjectId(2L);
        projects.add(project2);

        Map<Long, Project> result = projectService.convertProjectsToMap(projects);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey(1L));
        assertTrue(result.containsKey(2L));
    }

    @Test
    public void testGetOrganizationConfig() {
        when(mockEnvironment.containsProperty("organization.list")).thenReturn(true);
        when(mockEnvironment.getProperty("organization.list")).thenReturn("org1,org2");

        String result = projectService.getOrganizationConfig();

        assertEquals("org1,org2", result);
    }

    @Test
    public void testGetOrganizationConfigEmpty() {
        when(mockEnvironment.containsProperty("organization.list")).thenReturn(false);

        String result = projectService.getOrganizationConfig();

        assertEquals("", result);
    }

    @Test
    public void testIsSortOrderAscending() throws Exception {
        assertTrue(projectService.isSortOrderAscending(Optional.of("name")));
        assertFalse(projectService.isSortOrderAscending(Optional.of("-name")));
        assertFalse(projectService.isSortOrderAscending(Optional.empty()));
    }

    @Test
    public void testGetProjectSortComparator() throws Exception {
        assertNotNull(projectService.getProjectSortComparator(Optional.of("stars_count"), true));
        assertNotNull(projectService.getProjectSortComparator(Optional.of("score"), true));
        assertNotNull(projectService.getProjectSortComparator(Optional.of("commits_count"), true));
        assertNotNull(projectService.getProjectSortComparator(Optional.of("forks_count"), true));
        assertNotNull(projectService.getProjectSortComparator(Optional.of("contributors_count"), true));
        assertNotNull(projectService.getProjectSortComparator(Optional.of("unknown"), true));
        assertNotNull(projectService.getProjectSortComparator(Optional.empty(), true));
    }
}
