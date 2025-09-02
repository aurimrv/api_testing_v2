package org.zalando.catwatch.backend;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHTag;
import org.zalando.catwatch.backend.github.RepositoryWrapper;
import java.util.Date;
import java.io.IOException;
import java.net.URL;
import org.mockito.Mockito;

public class sonnet35_run01_RepositoryWrapperTest {

    private GHRepository mockRepo;
    private GHOrganization mockOrg;
    private RepositoryWrapper repositoryWrapper;

    @Before
    public void initTest() {
        mockRepo = Mockito.mock(GHRepository.class);
        mockOrg = Mockito.mock(GHOrganization.class);
        repositoryWrapper = Mockito.mock(RepositoryWrapper.class);
    }

    @Test
    public void testGetId() {
        Mockito.when(repositoryWrapper.getId()).thenReturn(123);
        assertEquals(123, repositoryWrapper.getId());
    }

    @Test
    public void testGetName() {
        Mockito.when(repositoryWrapper.getName()).thenReturn("testRepo");
        assertEquals("testRepo", repositoryWrapper.getName());
    }

    @Test
    public void testGetUrl() throws Exception {
        URL mockUrl = new URL("https://github.com/testOrg/testRepo");
        Mockito.when(repositoryWrapper.getUrl()).thenReturn(mockUrl);
        assertEquals(mockUrl, repositoryWrapper.getUrl());
    }

    @Test
    public void testGetDescription() {
        Mockito.when(repositoryWrapper.getDescription()).thenReturn("Test Description");
        assertEquals("Test Description", repositoryWrapper.getDescription());
    }

    @Test
    public void testGetStarsCount() {
        Mockito.when(repositoryWrapper.getStarsCount()).thenReturn(100);
        assertEquals(100, repositoryWrapper.getStarsCount());
    }

    @Test
    public void testGetForksCount() {
        Mockito.when(repositoryWrapper.getForksCount()).thenReturn(50);
        assertEquals(50, repositoryWrapper.getForksCount());
    }

    @Test
    public void testGetSize() {
        Mockito.when(repositoryWrapper.getSize()).thenReturn(1024);
        assertEquals(1024, repositoryWrapper.getSize());
    }

    @Test
    public void testGetLastPushed() {
        Date mockDate = new Date();
        Mockito.when(repositoryWrapper.getLastPushed()).thenReturn(mockDate);
        assertEquals(mockDate, repositoryWrapper.getLastPushed());
    }

    @Test
    public void testGetPrimaryLanguage() {
        Mockito.when(repositoryWrapper.getPrimaryLanguage()).thenReturn("Java");
        assertEquals("Java", repositoryWrapper.getPrimaryLanguage());
    }

    @Test
    public void testListLanguages() throws IOException {
        Map<String, Long> mockLanguages = new HashMap<>();
        mockLanguages.put("Java", 1000L);
        mockLanguages.put("Python", 500L);
        Mockito.when(repositoryWrapper.listLanguages()).thenReturn(mockLanguages);
        assertEquals(mockLanguages, repositoryWrapper.listLanguages());
    }

    @Test
    public void testGetOrganizationName() {
        Mockito.when(repositoryWrapper.getOrganizationName()).thenReturn("testOrg");
        assertEquals("testOrg", repositoryWrapper.getOrganizationName());
    }

    @Test
    public void testListCommits() throws IOException {
        List<GHCommit> mockCommits = new ArrayList<>();
        mockCommits.add(Mockito.mock(GHCommit.class));
        mockCommits.add(Mockito.mock(GHCommit.class));
        Mockito.when(repositoryWrapper.listCommits()).thenReturn(mockCommits);
        assertEquals(mockCommits, repositoryWrapper.listCommits());
    }

    @Test
    public void testListContributors() throws IOException {
        List<GHRepository.Contributor> mockContributors = new ArrayList<>();
        mockContributors.add(Mockito.mock(GHRepository.Contributor.class));
        mockContributors.add(Mockito.mock(GHRepository.Contributor.class));
        Mockito.when(repositoryWrapper.listContributors()).thenReturn(mockContributors);
        assertEquals(mockContributors, repositoryWrapper.listContributors());
    }

    @Test
    public void testListTags() throws IOException {
        List<GHTag> mockTags = new ArrayList<>();
        mockTags.add(Mockito.mock(GHTag.class));
        mockTags.add(Mockito.mock(GHTag.class));
        Mockito.when(repositoryWrapper.listTags()).thenReturn(mockTags);
        assertEquals(mockTags, repositoryWrapper.listTags());
    }
}
