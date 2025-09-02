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
        List<Contributor> contributors = new ArrayList<>();
        Contributor contributor = new Contributor();
        contributor.setName("John Doe");
        contributor.setOrganizationName("Org1");
        contributor.setUrl("http://example.com");
        contributor.setSnapshotDate(new Date());
        contributor.setOrganizationalCommitsCount(10);
        contributor.setPersonalCommitsCount(5);
        contributor.setOrganizationalProjectsCount(2);
        contributor.setPersonalProjectsCount(1);
        contributors.add(contributor);

        ContributorStats stats = new ContributorStats(contributors);

        assertEquals("John Doe", stats.getName());
        assertEquals("http://example.com", stats.getUrl());
        assertEquals(1, stats.getOrganizationName().size());
        assertEquals("Org1", stats.getOrganizationName().get(0));
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
    public void testConstructorWithMultipleContributors() {
        List<Contributor> contributors = new ArrayList<>();
        
        Contributor contributor1 = new Contributor();
        contributor1.setName("John Doe");
        contributor1.setOrganizationName("Org1");
        contributor1.setUrl("http://example.com");
        contributor1.setSnapshotDate(new Date(1000));
        contributor1.setOrganizationalCommitsCount(10);
        contributor1.setPersonalCommitsCount(5);
        contributor1.setOrganizationalProjectsCount(2);
        contributor1.setPersonalProjectsCount(1);
        contributors.add(contributor1);

        Contributor contributor2 = new Contributor();
        contributor2.setName("John Doe");
        contributor2.setOrganizationName("Org2");
        contributor2.setUrl("http://example.com");
        contributor2.setSnapshotDate(new Date(2000));
        contributor2.setOrganizationalCommitsCount(15);
        contributor2.setPersonalCommitsCount(7);
        contributor2.setOrganizationalProjectsCount(3);
        contributor2.setPersonalProjectsCount(2);
        contributors.add(contributor2);

        ContributorStats stats = new ContributorStats(contributors);

        assertEquals("John Doe", stats.getName());
        assertEquals("http://example.com", stats.getUrl());
        assertEquals(2, stats.getOrganizationName().size());
        assertTrue(stats.getOrganizationName().contains("Org1"));
        assertTrue(stats.getOrganizationName().contains("Org2"));
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
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyLoginId() {
        List<Contributor> contributors = new ArrayList<>();
        Contributor contributor = new Contributor();
        contributor.setName("");
        contributors.add(contributor);

        new ContributorStats(contributors);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithDifferentLoginIds() {
        List<Contributor> contributors = new ArrayList<>();
        
        Contributor contributor1 = new Contributor();
        contributor1.setName("user1");
        contributors.add(contributor1);

        Contributor contributor2 = new Contributor();
        contributor2.setName("user2");
        contributors.add(contributor2);

        new ContributorStats(contributors);
    }

    @Test
    public void testGetters() {
        List<Contributor> contributors = new ArrayList<>();
        Contributor contributor = new Contributor();
        contributor.setName("John Doe");
        contributor.setOrganizationName("Org1");
        contributor.setUrl("http://example.com");
        contributor.setSnapshotDate(new Date());
        contributor.setOrganizationalCommitsCount(10);
        contributor.setPersonalCommitsCount(5);
        contributor.setOrganizationalProjectsCount(2);
        contributor.setPersonalProjectsCount(1);
        contributors.add(contributor);

        ContributorStats stats = new ContributorStats(contributors);

        assertEquals("John Doe", stats.getName());
        assertEquals("http://example.com", stats.getUrl());
        assertEquals(1, stats.getOrganizationName().size());
        assertEquals("Org1", stats.getOrganizationName().get(0));
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
        List<Contributor> contributors = new ArrayList<>();
        Contributor contributor = new Contributor();
        contributor.setName("John Doe");
        contributor.setOrganizationName("Org1");
        contributor.setUrl("http://example.com");
        Date snapshotDate = new Date();
        contributor.setSnapshotDate(snapshotDate);
        contributors.add(contributor);

        ContributorStats stats = new ContributorStats(contributors);

        String expected = String.format("ContributorStats(John Doe, [Org1], http://example.com, [%s])", snapshotDate);
        assertEquals(expected, stats.toString());
    }

    @Test
    public void testBuildStats() {
        List<Contributor> contributors = new ArrayList<>();
        
        Contributor contributor1 = new Contributor();
        contributor1.setName("user1");
        contributor1.setName("John Doe");
        contributor1.setOrganizationName("Org1");
        contributor1.setUrl("http://example.com");
        contributor1.setSnapshotDate(new Date(1000));
        contributors.add(contributor1);

        Contributor contributor2 = new Contributor();
        contributor2.setName("user2");
        contributor2.setName("Jane Smith");
        contributor2.setOrganizationName("Org2");
        contributor2.setUrl("http://example.org");
        contributor2.setSnapshotDate(new Date(2000));
        contributors.add(contributor2);

        List<ContributorStats> statsList = ContributorStats.buildStats(contributors);

        assertEquals(2, statsList.size());
        assertEquals("John Doe", statsList.get(0).getName());
        assertEquals("Jane Smith", statsList.get(1).getName());
    }

    @Test
    public void testGetDistinctContributors() {
        List<Contributor> contributors = new ArrayList<>();
        
        Contributor contributor1 = new Contributor();
        contributor1.setName("user1");
        contributor1.setName("John Doe");
        contributors.add(contributor1);

        Contributor contributor2 = new Contributor();
        contributor2.setName("user2");
        contributor2.setName("Jane Smith");
        contributors.add(contributor2);

        Contributor contributor3 = new Contributor();
        contributor3.setName("user1");
        contributor3.setName("John Doe");
        contributors.add(contributor3);

        Map<String, List<Contributor>> distinctContributors = ContributorStats.getDistinctContributors(contributors);

        assertEquals(2, distinctContributors.size());
        assertTrue(distinctContributors.containsKey("John Doe"));
        assertTrue(distinctContributors.containsKey("Jane Smith"));
        assertEquals(2, distinctContributors.get("John Doe").size());
        assertEquals(1, distinctContributors.get("Jane Smith").size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDistinctContributorsWithEmptyLoginId() {
        List<Contributor> contributors = new ArrayList<>();
        Contributor contributor = new Contributor();
        contributor.setName("");
        contributors.add(contributor);

        ContributorStats.getDistinctContributors(contributors);
    }
}
