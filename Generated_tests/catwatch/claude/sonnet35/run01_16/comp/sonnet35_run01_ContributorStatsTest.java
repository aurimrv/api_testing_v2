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
        controller.resetDatabase(java.util.Arrays.asList("CONTRIBUTOR"));
        controller.resetStateOfSUT();
    }

    @Test
    public void testConstructor() {
        List<Contributor> contributions = new ArrayList<>();
        Contributor contributor = new Contributor();
        contributor.setName("John Doe");
        contributor.setOrganizationalCommitsCount(10);
        contributor.setPersonalCommitsCount(5);
        contributor.setOrganizationalProjectsCount(2);
        contributor.setPersonalProjectsCount(1);
        contributions.add(contributor);

        ContributorStats stats = new ContributorStats(contributions);

        assertNotNull(stats);
        assertEquals("John Doe", stats.getName());
        assertEquals(1, stats.getOrganizationName().size());
        assertEquals(1, stats.getOrganizationalCommitsCounts().size());
        assertEquals(10, (int) stats.getOrganizationalCommitsCounts().get(0));
        assertEquals(1, stats.getPersonalCommitsCounts().size());
        assertEquals(5, (int) stats.getPersonalCommitsCounts().get(0));
        assertEquals(1, stats.getOrganizationalProjectsCounts().size());
        assertEquals(2, (int) stats.getOrganizationalProjectsCounts().get(0));
        assertEquals(1, stats.getPersonalProjectsCounts().size());
        assertEquals(1, (int) stats.getPersonalProjectsCounts().get(0));
        assertEquals(1, stats.getSnapshotDates().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyLoginId() {
        List<Contributor> contributions = new ArrayList<>();
        Contributor contributor = new Contributor();
        contributions.add(contributor);

        new ContributorStats(contributions);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithDifferentLoginIds() {
        List<Contributor> contributions = new ArrayList<>();
        Contributor contributor1 = new Contributor();
        contributions.add(contributor1);

        Contributor contributor2 = new Contributor();
        contributions.add(contributor2);

        new ContributorStats(contributions);
    }

    @Test
    public void testGetDistinctContributors() {
        List<Contributor> contributors = new ArrayList<>();
        Contributor contributor1 = new Contributor();
        contributors.add(contributor1);

        Contributor contributor2 = new Contributor();
        contributors.add(contributor2);

        Contributor contributor3 = new Contributor();
        contributors.add(contributor3);

        Map<String, List<Contributor>> result = ContributorStats.getDistinctContributors(contributors);

        assertEquals(3, result.size());
    }

    @Test
    public void testBuildStats() {
        List<Contributor> contributors = new ArrayList<>();
        Contributor contributor1 = new Contributor();
        contributor1.setName("User One");
        contributor1.setOrganizationName("Org1");
        contributors.add(contributor1);

        Contributor contributor2 = new Contributor();
        contributor2.setName("User Two");
        contributor2.setOrganizationName("Org2");
        contributors.add(contributor2);

        List<ContributorStats> stats = ContributorStats.buildStats(contributors);

        assertEquals(2, stats.size());
        assertEquals("User One", stats.get(0).getName());
        assertEquals("User Two", stats.get(1).getName());
    }

    @Test
    public void testToString() {
        List<Contributor> contributions = new ArrayList<>();
        Contributor contributor = new Contributor();
        contributor.setName("John Doe");
        contributor.setOrganizationName("TestOrg");
        contributions.add(contributor);

        ContributorStats stats = new ContributorStats(contributions);

        String result = stats.toString();
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("TestOrg"));
    }
}
