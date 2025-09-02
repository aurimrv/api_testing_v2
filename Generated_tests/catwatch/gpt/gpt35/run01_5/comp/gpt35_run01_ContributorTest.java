
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

public class gpt35_run01_ContributorTest {

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
    public void test_Contributor_Constructor() {
        org.zalando.catwatch.backend.model.Contributor contributor = new org.zalando.catwatch.backend.model.Contributor();
        assertNotNull(contributor);
    }

    @Test
    public void test_Contributor_getId() {
        org.zalando.catwatch.backend.model.Contributor contributor = new org.zalando.catwatch.backend.model.Contributor();
        assertEquals(0, contributor.getId());
    }

    @Test
    public void test_Contributor_getOrganizationId() {
        org.zalando.catwatch.backend.model.Contributor contributor = new org.zalando.catwatch.backend.model.Contributor();
        assertEquals(0, contributor.getOrganizationId());
    }

    @Test
    public void test_Contributor_getSnapshotDate() {
        org.zalando.catwatch.backend.model.Contributor contributor = new org.zalando.catwatch.backend.model.Contributor();
        assertNull(contributor.getSnapshotDate());
    }

    @Test
    public void test_Contributor_getLoginId() {
        org.zalando.catwatch.backend.model.Contributor contributor = new org.zalando.catwatch.backend.model.Contributor();
        contributor.setUrl("https://github.com/testUser");
        assertEquals("testUser", contributor.getLoginId());
    }

    @Test
    public void test_Contributor_toString() {
        org.zalando.catwatch.backend.model.Contributor contributor = new org.zalando.catwatch.backend.model.Contributor();
        String expected = "class Contributor {\n" +
                "  id: 0\n" +
                "  organizationId: 0\n" +
                "  name: null\n" +
                "  url: null\n" +
                "  organizationalCommitsCount: null\n" +
                "  personalCommitsCount: null\n" +
                "  personalProjectsCount: null\n" +
                "  organizationalProjectsCount: null\n" +
                "  organizationName: null\n" +
                "  snapshotDate: null\n" +
                "}\n";
        assertEquals(expected, contributor.toString());
    }

    @Test
    public void test_QContributor_clinit() {
        org.zalando.catwatch.backend.model.QContributor qContributor = new org.zalando.catwatch.backend.model.QContributor("qContributor");
        assertNotNull(qContributor);
    }

    @Test
    public void test_QContributor_Constructor() {
        org.zalando.catwatch.backend.model.QContributor qContributor = new org.zalando.catwatch.backend.model.QContributor("qContributor");
        assertNotNull(qContributor);
    }
}
