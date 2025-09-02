package org.zalando.catwatch.backend;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.List;
import org.evomaster.client.java.controller.SutHandler;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import static io.restassured.config.RedirectConfig.redirectConfig;
import org.zalando.catwatch.backend.model.Contributor;
import org.zalando.catwatch.backend.util.ContributorStats;
import java.util.ArrayList;
import java.util.Date;
import java.util.Arrays;

public class sonnet35_run01_ContributorStatsTest {

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
    public void testConstructor() {
        List<Contributor> contributions = new ArrayList<>();
        Contributor contributor = new Contributor();
        contributor.setName("John Doe");
        contributor.setId("johndoe");
        contributor.setUrl("https://github.com/johndoe");
        contributor.setOrganizationName("TestOrg");
        contributor.setOrganizationalCommitsCount(10);
        contributor.setPersonalCommitsCount(5);
        contributor.setOrganizationalProjectsCount(2);
        contributor.setPersonalProjectsCount(1);
        contributor.setSnapshotDate(new Date());
        contributions.add(contributor);

        ContributorStats stats = new ContributorStats(contributions);

        assertNotNull(stats);
        assertEquals("John Doe", stats.getName());
        assertEquals("https://github.com/johndoe", stats.getUrl());
        assertEquals(1, stats.getOrganizationName().size());
        assertEquals("TestOrg", stats.getOrganizationName().get(0));
        assertEquals(1, stats.getOrganizationalCommitsCounts().size());
        assertEquals(Integer.valueOf(10), stats.getOrganizationalCommitsCounts().get(0));
        assertEquals(1, stats.getPersonalCommitsCounts().size());
        assertEquals(Integer.valueOf(5), stats.getPersonalCommitsCounts().get(0));
        assertEquals(1, stats.getOrganizationalProjectsCounts().size());
        assertEquals(Integer.valueOf(2), stats.getOrganizationalProjectsCounts().get(0));
        assertEquals(1, stats.getPersonalProjectsCounts().size());
        assertEquals(Integer.valueOf(1), stats.getPersonalProjectsCounts().get(0));
        assertEquals(1, stats.getSnapshotDates().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyLoginId() {
        List<Contributor> contributions = new ArrayList<>();
        Contributor contributor = new Contributor();
        contributor.setId("");
        contributions.add(contributor);

        new ContributorStats(contributions);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithDifferentLoginIds() {
        List<Contributor> contributions = new ArrayList<>();
        Contributor contributor1 = new Contributor();
        contributor1.setId("user1");
        contributions.add(contributor1);

        Contributor contributor2 = new Contributor();
        contributor2.setId("user2");
        contributions.add(contributor2);

        new ContributorStats(contributions);
    }

    @Test
    public void testConstructorWithMultipleContributions() {
        List<Contributor> contributions = new ArrayList<>();
        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 86400000); // Next day

        Contributor contributor1 = new Contributor();
        contributor1.setName("John Doe");
        contributor1.setId("johndoe");
        contributor1.setUrl("https://github.com/johndoe");
        contributor1.setOrganizationName("TestOrg1");
        contributor1.setOrganizationalCommitsCount(10);
        contributor1.setPersonalCommitsCount(5);
        contributor1.setOrganizationalProjectsCount(2);
        contributor1.setPersonalProjectsCount(1);
        contributor1.setSnapshotDate(date1);
        contributions.add(contributor1);

        Contributor contributor2 = new Contributor();
        contributor2.setName("John Doe");
        contributor2.setId("johndoe");
        contributor2.setUrl("https://github.com/johndoe");
        contributor2.setOrganizationName("TestOrg2");
        contributor2.setOrganizationalCommitsCount(15);
        contributor2.setPersonalCommitsCount(7);
        contributor2.setOrganizationalProjectsCount(3);
        contributor2.setPersonalProjectsCount(2);
        contributor2.setSnapshotDate(date2);
        contributions.add(contributor2);

        ContributorStats stats = new ContributorStats(contributions);

        assertNotNull(stats);
        assertEquals("John Doe", stats.getName());
        assertEquals("https://github.com/johndoe", stats.getUrl());
        assertEquals(2, stats.getOrganizationName().size());
        assertTrue(stats.getOrganizationName().contains("TestOrg1"));
        assertTrue(stats.getOrganizationName().contains("TestOrg2"));
        assertEquals(2, stats.getOrganizationalCommitsCounts().size());
        assertEquals(Integer.valueOf(10), stats.getOrganizationalCommitsCounts().get(0));
        assertEquals(Integer.valueOf(15), stats.getOrganizationalCommitsCounts().get(1));
        assertEquals(2, stats.getPersonalCommitsCounts().size());
        assertEquals(Integer.valueOf(5), stats.getPersonalCommitsCounts().get(0));
        assertEquals(Integer.valueOf(7), stats.getPersonalCommitsCounts().get(1));
        assertEquals(2, stats.getOrganizationalProjectsCounts().size());
        assertEquals(Integer.valueOf(2), stats.getOrganizationalProjectsCounts().get(0));
        assertEquals(Integer.valueOf(3), stats.getOrganizationalProjectsCounts().get(1));
        assertEquals(2, stats.getPersonalProjectsCounts().size());
        assertEquals(Integer.valueOf(1), stats.getPersonalProjectsCounts().get(0));
        assertEquals(Integer.valueOf(2), stats.getPersonalProjectsCounts().get(1));
        assertEquals(2, stats.getSnapshotDates().size());
        assertEquals(date1, stats.getSnapshotDates().get(0));
        assertEquals(date2, stats.getSnapshotDates().get(1));
    }

    @Test
    public void testGetters() {
        List<Contributor> contributions = new ArrayList<>();
        Contributor contributor = new Contributor();
        contributor.setName("John Doe");
        contributor.setId("johndoe");
        contributor.setUrl("https://github.com/johndoe");
        contributor.setOrganizationName("TestOrg");
        contributor.setOrganizationalCommitsCount(10);
        contributor.setPersonalCommitsCount(5);
        contributor.setOrganizationalProjectsCount(2);
        contributor.setPersonalProjectsCount(1);
        contributor.setSnapshotDate(new Date());
        contributions.add(contributor);

        ContributorStats stats = new ContributorStats(contributions);

        assertEquals("John Doe", stats.getName());
        assertEquals("https://github.com/johndoe", stats.getUrl());
        assertEquals(1, stats.getOrganizationName().size());
        assertEquals("TestOrg", stats.getOrganizationName().get(0));
        assertEquals(1, stats.getOrganizationalCommitsCounts().size());
        assertEquals(Integer.valueOf(10), stats.getOrganizationalCommitsCounts().get(0));
        assertEquals(1, stats.getPersonalCommitsCounts().size());
        assertEquals(Integer.valueOf(5), stats.getPersonalCommitsCounts().get(0));
        assertEquals(1, stats.getOrganizationalProjectsCounts().size());
        assertEquals(Integer.valueOf(2), stats.getOrganizationalProjectsCounts().get(0));
        assertEquals(1, stats.getPersonalProjectsCounts().size());
        assertEquals(Integer.valueOf(1), stats.getPersonalProjectsCounts().get(0));
        assertEquals(1, stats.getSnapshotDates().size());
    }

    @Test
    public void testToString() {
        List<Contributor> contributions = new ArrayList<>();
        Date date = new Date();
        Contributor contributor = new Contributor();
        contributor.setName("John Doe");
        contributor.setId("johndoe");
        contributor.setUrl("https://github.com/johndoe");
        contributor.setOrganizationName("TestOrg");
        contributor.setSnapshotDate(date);
        contributions.add(contributor);

        ContributorStats stats = new ContributorStats(contributions);

        String expected = String.format("ContributorStats(John Doe, [TestOrg], https://github.com/johndoe, [%s])", date);
        assertEquals(expected, stats.toString());
    }

    @Test
    public void testGetDistinctContributors() {
        List<Contributor> contributors = new ArrayList<>();
        Contributor contributor1 = new Contributor();
        contributor1.setId("user1");
        contributors.add(contributor1);

        Contributor contributor2 = new Contributor();
        contributor2.setId("user2");
        contributors.add(contributor2);

        Contributor contributor3 = new Contributor();
        contributor3.setId("user1");
        contributors.add(contributor3);

        Map<String, List<Contributor>> result = ContributorStats.getDistinctContributors(contributors);

        assertEquals(2, result.size());
        assertTrue(result.containsKey("user1"));
        assertTrue(result.containsKey("user2"));
        assertEquals(2, result.get("user1").size());
        assertEquals(1, result.get("user2").size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDistinctContributorsWithEmptyLoginId() {
        List<Contributor> contributors = new ArrayList<>();
        Contributor contributor = new Contributor();
        contributor.setId("");
        contributors.add(contributor);

        ContributorStats.getDistinctContributors(contributors);
    }

    @Test
    public void testBuildStats() {
        List<Contributor> contributors = new ArrayList<>();
        Contributor contributor1 = new Contributor();
        contributor1.setId("user1");
        contributor1.setName("User One");
        contributor1.setUrl("https://github.com/user1");
        contributor1.setSnapshotDate(new Date());
        contributors.add(contributor1);

        Contributor contributor2 = new Contributor();
        contributor2.setId("user2");
        contributor2.setName("User Two");
        contributor2.setUrl("https://github.com/user2");
        contributor2.setSnapshotDate(new Date());
        contributors.add(contributor2);

        List<ContributorStats> stats = ContributorStats.buildStats(contributors);

        assertEquals(2, stats.size());
        assertEquals("User One", stats.get(0).getName());
        assertEquals("https://github.com/user1", stats.get(0).getUrl());
        assertEquals("User Two", stats.get(1).getName());
        assertEquals("https://github.com/user2", stats.get(1).getUrl());
    }
}
