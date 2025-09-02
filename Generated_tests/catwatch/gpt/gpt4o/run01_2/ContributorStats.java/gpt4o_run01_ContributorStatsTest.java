
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;
import org.zalando.catwatch.backend.util.ContributorStats;
import org.zalando.catwatch.backend.model.Contributor;

public class gpt4o_run01_ContributorStatsTest {

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
    public void testConstructorAndFields() {
        Contributor contributor1 = new Contributor(1L, 1L, new Date());
        Contributor contributor2 = new Contributor(2L, 2L, new Date());
        List<Contributor> contributors = Arrays.asList(contributor1, contributor2);

        ContributorStats stats = new ContributorStats(contributors);

        assertNotNull(stats);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithEmptyLoginId() {
        Contributor contributor1 = new Contributor(null, 0L, new Date());
        List<Contributor> contributors = Arrays.asList(contributor1);

        new ContributorStats(contributors);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithDifferentLoginIds() {
        Contributor contributor1 = new Contributor(1L, 1L, new Date());
        Contributor contributor2 = new Contributor(2L, 2L, new Date());
        List<Contributor> contributors = Arrays.asList(contributor1, contributor2);

        new ContributorStats(contributors);
    }

    @Test
    public void testGetDistinctContributors() {
        Contributor contributor1 = new Contributor(1L, 1L, new Date());
        Contributor contributor2 = new Contributor(2L, 2L, new Date());
        List<Contributor> contributors = Arrays.asList(contributor1, contributor2);

        Map<String, List<Contributor>> distinctContributors = ContributorStats.getDistinctContributors(contributors);

        assertNotNull(distinctContributors);
    }

    @Test
    public void testBuildStats() {
        Contributor contributor1 = new Contributor(1L, 1L, new Date());
        Contributor contributor2 = new Contributor(2L, 2L, new Date());
        List<Contributor> contributors = Arrays.asList(contributor1, contributor2);

        List<ContributorStats> stats = ContributorStats.buildStats(contributors);

        assertNotNull(stats);
    }

    @Test
    public void testToString() {
        Contributor contributor1 = new Contributor(1L, 1L, new Date());
        List<Contributor> contributors = Arrays.asList(contributor1);

        ContributorStats stats = new ContributorStats(contributors);

        assertNotNull(stats.toString());
    }
}
