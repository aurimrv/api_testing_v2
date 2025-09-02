
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
import java.util.Date; // Added import for Date

import org.zalando.catwatch.backend.repo.builder.ContributorBuilder;
import org.zalando.catwatch.backend.model.Contributor; // Added import for Contributor

public class gpt35_run01_ContributorBuilderTest {

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
        ContributorBuilder cb = new ContributorBuilder();
        assertNotNull(cb);
    }

    @Test
    public void testId() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.id(1234);
        assertNotNull(result);
    }

    @Test
    public void testUrl() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.url("https://test.com");
        assertNotNull(result);
    }

    @Test
    public void testName() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.name("Test Name");
        assertNotNull(result);
    }

    @Test
    public void testDays() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.days(5);
        assertNotNull(result);
    }

    @Test
    public void testOrganizationName() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.organizationName("Test Organization");
        assertNotNull(result);
    }

    @Test
    public void testOrganizationId() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.organizationId(9876);
        assertNotNull(result);
    }

    @Test
    public void testOrgCommits() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.orgCommits(50);
        assertNotNull(result);
    }

    @Test
    public void testOrgProjects() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.orgProjects(5);
        assertNotNull(result);
    }

    @Test
    public void testPersProjects() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.persProjects(3);
        assertNotNull(result);
    }

    @Test
    public void testPersCommits() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.persCommits(6);
        assertNotNull(result);
    }

    @Test
    public void testSnapshotDate() {
        ContributorBuilder cb = new ContributorBuilder();
        ContributorBuilder result = cb.snapshotDate(new Date());
        assertNotNull(result);
    }

    @Test
    public void testCreate() {
        ContributorBuilder cb = new ContributorBuilder();
        Contributor result = cb.create();
        assertNotNull(result);
    }

    @Test
    public void testSave() {
        ContributorBuilder cb = new ContributorBuilder();
        Contributor result = cb.save();
        assertNotNull(result);
    }
}
