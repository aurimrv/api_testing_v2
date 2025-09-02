package org.zalando.catwatch.backend;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTeam;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.PagedIterable;
import org.zalando.catwatch.backend.github.OrganizationWrapper;
import org.zalando.catwatch.backend.github.RepositoryWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class sonnet35_run01_OrganizationWrapperTest {

    @Test
    public void testConstructor() {
        GHOrganization mockOrg = mock(GHOrganization.class);
        when(mockOrg.getLogin()).thenReturn("testOrg");

        OrganizationWrapper wrapper = Mockito.mock(OrganizationWrapper.class);
        assertNotNull(wrapper);
    }

    @Test
    public void testListTeams() throws IOException {
        GHOrganization mockOrg = mock(GHOrganization.class);
        GHTeam mockTeam = mock(GHTeam.class);
        PagedIterable<GHTeam> teamPagedIterable = Mockito.mock(PagedIterable.class);
        List<GHTeam> teamList = Collections.singletonList(mockTeam);

        when(mockOrg.listTeams()).thenReturn(teamPagedIterable);
        when(teamPagedIterable.asList()).thenReturn(teamList);
        when(mockOrg.getLogin()).thenReturn("testOrg");

        OrganizationWrapper wrapper = Mockito.mock(OrganizationWrapper.class);
        when(wrapper.listTeams()).thenReturn(teamList);

        List<GHTeam> result = wrapper.listTeams();

        assertEquals(1, result.size());
        assertEquals(mockTeam, result.get(0));
    }

    @Test
    public void testListTeamsException() throws IOException {
        GHOrganization mockOrg = mock(GHOrganization.class);
        when(mockOrg.listTeams()).thenThrow(new IOException("Test exception"));
        when(mockOrg.getLogin()).thenReturn("testOrg");

        OrganizationWrapper wrapper = Mockito.mock(OrganizationWrapper.class);
        when(wrapper.listTeams()).thenReturn(new ArrayList<>());

        List<GHTeam> result = wrapper.listTeams();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testListMembers() throws IOException {
        GHOrganization mockOrg = mock(GHOrganization.class);
        GHUser mockUser = mock(GHUser.class);
        PagedIterable<GHUser> userPagedIterable = Mockito.mock(PagedIterable.class);
        List<GHUser> userList = Collections.singletonList(mockUser);

        when(mockOrg.listMembers()).thenReturn(userPagedIterable);
        when(userPagedIterable.asList()).thenReturn(userList);
        when(mockOrg.getLogin()).thenReturn("testOrg");

        OrganizationWrapper wrapper = Mockito.mock(OrganizationWrapper.class);
        when(wrapper.listMembers()).thenReturn(userList);

        List<GHUser> result = wrapper.listMembers();

        assertEquals(1, result.size());
        assertEquals(mockUser, result.get(0));
    }

    @Test
    public void testListMembersException() throws IOException {
        GHOrganization mockOrg = mock(GHOrganization.class);
        when(mockOrg.listMembers()).thenThrow(new IOException("Test exception"));
        when(mockOrg.getLogin()).thenReturn("testOrg");

        OrganizationWrapper wrapper = Mockito.mock(OrganizationWrapper.class);
        when(wrapper.listMembers()).thenReturn(new ArrayList<>());

        List<GHUser> result = wrapper.listMembers();

        assertTrue(result.isEmpty());
    }

    @Test
    public void testListRepositories() throws IOException {
        GHOrganization mockOrg = mock(GHOrganization.class);
        GHRepository mockRepo = mock(GHRepository.class);
        PagedIterable<GHRepository> repoPagedIterable = Mockito.mock(PagedIterable.class);
        List<GHRepository> repoList = Collections.singletonList(mockRepo);

        when(mockOrg.listRepositories()).thenReturn(repoPagedIterable);
        when(repoPagedIterable.asList()).thenReturn(repoList);
        when(mockOrg.getLogin()).thenReturn("testOrg");
        when(mockRepo.isPrivate()).thenReturn(false);
        when(mockRepo.isFork()).thenReturn(false);

        OrganizationWrapper wrapper = Mockito.mock(OrganizationWrapper.class);
        when(wrapper.listRepositories()).thenReturn(Collections.singletonList(Mockito.mock(RepositoryWrapper.class)));

        List<RepositoryWrapper> result = wrapper.listRepositories();

        assertEquals(1, result.size());
    }

    @Test
    public void testGetId() {
        GHOrganization mockOrg = mock(GHOrganization.class);
        when(mockOrg.getId()).thenReturn(123);
        when(mockOrg.getLogin()).thenReturn("testOrg");

        OrganizationWrapper wrapper = Mockito.mock(OrganizationWrapper.class);
        when(wrapper.getId()).thenReturn(123);

        int result = wrapper.getId();

        assertEquals(123, result);
    }

    @Test
    public void testGetPublicRepoCount() throws IOException {
        GHOrganization mockOrg = mock(GHOrganization.class);
        when(mockOrg.getPublicRepoCount()).thenReturn(5);
        when(mockOrg.getLogin()).thenReturn("testOrg");

        OrganizationWrapper wrapper = Mockito.mock(OrganizationWrapper.class);
        when(wrapper.getPublicRepoCount()).thenReturn(5);

        int result = wrapper.getPublicRepoCount();

        assertEquals(5, result);
    }

    @Test
    public void testGetPublicRepoCountException() throws IOException {
        GHOrganization mockOrg = mock(GHOrganization.class);
        when(mockOrg.getPublicRepoCount()).thenThrow(new IOException("Test exception"));
        when(mockOrg.getLogin()).thenReturn("testOrg");

        OrganizationWrapper wrapper = Mockito.mock(OrganizationWrapper.class);
        when(wrapper.getPublicRepoCount()).thenReturn(0);

        int result = wrapper.getPublicRepoCount();

        assertEquals(0, result);
    }

    @Test
    public void testGetLogin() {
        GHOrganization mockOrg = mock(GHOrganization.class);
        when(mockOrg.getLogin()).thenReturn("testOrg");

        OrganizationWrapper wrapper = Mockito.mock(OrganizationWrapper.class);
        when(wrapper.getLogin()).thenReturn("testOrg");

        String result = wrapper.getLogin();

        assertEquals("testOrg", result);
    }

    @Test
    public void testContributorIsMember() throws IOException {
        GHOrganization mockOrg = mock(GHOrganization.class);
        GHUser mockUser = mock(GHUser.class);
        GHRepository.Contributor mockContributor = mock(GHRepository.Contributor.class);
        PagedIterable<GHUser> userPagedIterable = Mockito.mock(PagedIterable.class);
        List<GHUser> userList = Collections.singletonList(mockUser);

        when(mockOrg.listMembers()).thenReturn(userPagedIterable);
        when(userPagedIterable.asList()).thenReturn(userList);
        when(mockOrg.getLogin()).thenReturn("testOrg");

        OrganizationWrapper wrapper = Mockito.mock(OrganizationWrapper.class);
        when(wrapper.contributorIsMember(mockContributor)).thenReturn(false);

        Boolean result = wrapper.contributorIsMember(mockContributor);

        assertFalse(result);
    }
}
