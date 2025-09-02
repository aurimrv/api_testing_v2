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
        when(mockProjectRepository.findProjects(anyString(), any(Date.class), any(), any()))
            .thenReturn(mockProjects);

        when(mockEnvironment.containsProperty(anyString())).thenReturn(true);
        when(mockEnvironment.getProperty(anyString())).thenReturn("org1,org2");

        Iterable<Project> result = projectService.findProjects(organizations, limit, offset, startDate, endDate, sortBy, query, language);

        assertNotNull(result);
        verify(mockProjectRepository, times(4)).findProjects(anyString(), any(Date.class), any(), any());
    }

    @Test
    public void testCreateMergedProject() {
        Project startProject = new Project();
        startProject.setStarsCount(10);
        startProject.setCommitsCount(100);
        startProject.setForksCount(5);
        startProject.setContributorsCount(20);
        startProject.setScore(50);
        startProject.setSnapshotDate(new Date());

        Project endProject = new Project();
        endProject.setStarsCount(20);
        endProject.setCommitsCount(200);
        endProject.setForksCount(10);
        endProject.setContributorsCount(30);
        endProject.setScore(100);
        endProject.setSnapshotDate(new Date());

        List<Project> mockProjects = Arrays.asList(startProject);
        when(mockProjectRepository.findProjects(anyString(), any(Date.class), any(), any()))
            .thenReturn(mockProjects);

        Project result = projectService.findProjects(null, Optional.empty(), Optional.empty(), Optional.of(startProject.getSnapshotDate()), Optional.of(endProject.getSnapshotDate()), Optional.empty(), Optional.empty(), Optional.empty()).iterator().next();

        assertNotNull(result);
        assertEquals(10, result.getStarsCount().intValue());
        assertEquals(100, result.getCommitsCount().intValue());
        assertEquals(5, result.getForksCount().intValue());
        assertEquals(20, result.getContributorsCount().intValue());
        assertEquals(50, result.getScore().intValue());
    }

    @Test
    public void testGetOrganizationConfig() {
        when(mockEnvironment.containsProperty("organization.list")).thenReturn(true);
        when(mockEnvironment.getProperty("organization.list")).thenReturn("org1,org2");

        Project mockProject = new Project();
        mockProject.setOrganizationName("org1,org2");
        List<Project> mockProjects = Arrays.asList(mockProject);
        when(mockProjectRepository.findProjects(anyString(), any(Date.class), any(), any()))
            .thenReturn(mockProjects);

        String result = projectService.findProjects(null, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()).iterator().next().getOrganizationName();

        assertEquals("org1,org2", result);
    }

    @Test
    public void testGetOrganizationConfigEmpty() {
        when(mockEnvironment.containsProperty("organization.list")).thenReturn(false);

        Project mockProject = new Project();
        mockProject.setOrganizationName("");
        List<Project> mockProjects = Arrays.asList(mockProject);
        when(mockProjectRepository.findProjects(anyString(), any(Date.class), any(), any()))
            .thenReturn(mockProjects);

        String result = projectService.findProjects(null, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()).iterator().next().getOrganizationName();

        assertEquals("", result);
    }
}
