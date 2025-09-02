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
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import org.zalando.catwatch.backend.model.Contributor;
import org.zalando.catwatch.backend.util.ContributorStats;

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
        contributor.setOrganizationName("TestOrg");
        contributor.setOrganizationalCommitsCount(10);
        contributor.setPersonalCommitsCount(5);
        contributor.setOrganizationalProjectsCount(2);
        contributor.setPersonalProjectsCount(1);
        contributors.add(contributor);

        ContributorStats stats = new ContributorStats(contributors);

        assertNotNull(stats);
        assertEquals("John Doe", stats.getName());
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
    public void testGetters() {
        List<Contributor> contributors = new ArrayList<>();
        Contributor contributor = new Contributor();
        contributor.setName("Jane Doe");
        contributor.setOrganizationName("TestOrg");
        contributor.setOrganizationalCommitsCount(20);
        contributor.setPersonalCommitsCount(10);
        contributor.setOrganizationalProjectsCount(3);
        contributor.setPersonalProjectsCount(2);
        contributors.add(contributor);

        ContributorStats stats = new ContributorStats(contributors);

        assertEquals("Jane Doe", stats.getName());
        assertEquals(1, stats.getOrganizationName().size());
        assertEquals("TestOrg", stats.getOrganizationName().get(0));
        assertEquals(1, stats.getOrganizationalCommitsCounts().size());
        assertEquals(Integer.valueOf(20), stats.getOrganizationalCommitsCounts().get(0));
        assertEquals(1, stats.getPersonalCommitsCounts().size());
        assertEquals(Integer.valueOf(10), stats.getPersonalCommitsCounts().get(0));
        assertEquals(1, stats.getOrganizationalProjectsCounts().size());
        assertEquals(Integer.valueOf(3), stats.getOrganizationalProjectsCounts().get(0));
        assertEquals(1, stats.getPersonalProjectsCounts().size());
        assertEquals(Integer.valueOf(2), stats.getPersonalProjectsCounts().get(0));
        assertEquals(1, stats.getSnapshotDates().size());
    }

    @Test
    public void testMultipleContributors() {
        List<Contributor> contributors = new ArrayList<>();
        Contributor contributor1 = new Contributor();
        contributor1.setName("Alice");
        contributor1.setOrganizationName("OrgA");
        contributor1.setOrganizationalCommitsCount(30);
        contributor1.setPersonalCommitsCount(15);
        contributor1.setOrganizationalProjectsCount(4);
        contributor1.setPersonalProjectsCount(2);
        contributors.add(contributor1);

        Contributor contributor2 = new Contributor();
        contributor2.setName("Alice");
        contributor2.setOrganizationName("OrgB");
        contributor2.setOrganizationalCommitsCount(20);
        contributor2.setPersonalCommitsCount(10);
        contributor2.setOrganizationalProjectsCount(3);
        contributor2.setPersonalProjectsCount(1);
        contributors.add(contributor2);

        ContributorStats stats = new ContributorStats(contributors);

        assertEquals("Alice", stats.getName());
        assertEquals(2, stats.getOrganizationName().size());
        assertTrue(stats.getOrganizationName().contains("OrgA"));
        assertTrue(stats.getOrganizationName().contains("OrgB"));
        assertEquals(1, stats.getOrganizationalCommitsCounts().size());
        assertEquals(Integer.valueOf(50), stats.getOrganizationalCommitsCounts().get(0));
        assertEquals(1, stats.getPersonalCommitsCounts().size());
        assertEquals(Integer.valueOf(15), stats.getPersonalCommitsCounts().get(0));
        assertEquals(1, stats.getOrganizationalProjectsCounts().size());
        assertEquals(Integer.valueOf(7), stats.getOrganizationalProjectsCounts().get(0));
        assertEquals(1, stats.getPersonalProjectsCounts().size());
        assertEquals(Integer.valueOf(3), stats.getPersonalProjectsCounts().get(0));
        assertEquals(1, stats.getSnapshotDates().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLoginId() {
        List<Contributor> contributors = new ArrayList<>();
        Contributor contributor = new Contributor();
        contributor.setName("Bob");
        contributors.add(contributor);

        new ContributorStats(contributors);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferentLoginIds() {
        List<Contributor> contributors = new ArrayList<>();
        Contributor contributor1 = new Contributor();
        contributor1.setName("Charlie");
        contributors.add(contributor1);

        Contributor contributor2 = new Contributor();
        contributor2.setName("David");
        contributors.add(contributor2);

        new ContributorStats(contributors);
    }

    @Test
    public void testToString() {
        List<Contributor> contributors = new ArrayList<>();
        Contributor contributor = new Contributor();
        contributor.setName("Eve");
        contributor.setOrganizationName("TestOrg");
        contributors.add(contributor);

        ContributorStats stats = new ContributorStats(contributors);

        String toString = stats.toString();
        assertTrue(toString.contains("Eve"));
        assertTrue(toString.contains("TestOrg"));
    }

    @Test
    public void testGetDistinctContributors() {
        List<Contributor> contributors = new ArrayList<>();
        Contributor contributor1 = new Contributor();
        contributor1.setName("Frank");
        contributors.add(contributor1);

        Contributor contributor2 = new Contributor();
        contributor2.setName("Grace");
        contributors.add(contributor2);

        Contributor contributor3 = new Contributor();
        contributor3.setName("Frank");
        contributors.add(contributor3);

        Map<String, List<Contributor>> distinctContributors = ContributorStats.getDistinctContributors(contributors);

        assertEquals(2, distinctContributors.size());
        assertTrue(distinctContributors.containsKey("Frank"));
        assertTrue(distinctContributors.containsKey("Grace"));
        assertEquals(2, distinctContributors.get("Frank").size());
        assertEquals(1, distinctContributors.get("Grace").size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDistinctContributorsEmptyLoginId() {
        List<Contributor> contributors = new ArrayList<>();
        Contributor contributor = new Contributor();
        contributor.setName("Henry");
        contributors.add(contributor);

        ContributorStats.getDistinctContributors(contributors);
    }
}
