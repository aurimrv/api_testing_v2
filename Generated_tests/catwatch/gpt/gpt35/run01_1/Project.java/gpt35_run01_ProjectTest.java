
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
import static io.restassured.RestAssured.given;
import io.restassured.response.ValidatableResponse;
import org.evomaster.client.java.controller.api.dto.database.operations.InsertionResultsDto;
import org.evomaster.client.java.controller.api.dto.database.operations.InsertionDto;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import static io.restassured.config.RedirectConfig.redirectConfig;
import org.evomaster.client.java.controller.contentMatchers.NumberMatcher;
import org.evomaster.client.java.controller.contentMatchers.StringMatcher;
import org.evomaster.client.java.controller.contentMatchers.SubStringMatcher;
import org.evomaster.client.java.controller.expect.ExpectationHandler;
import io.restassured.path.json.JsonPath;
import java.util.Arrays;

public class gpt35_run01_ProjectTest {

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
    public void testProjectGetId() {
        org.zalando.catwatch.backend.model.Project project = new org.zalando.catwatch.backend.model.Project();
        project.setId(1);
        assertEquals(1, project.getId());
    }

    @Test
    public void testProjectSetId() {
        org.zalando.catwatch.backend.model.Project project = new org.zalando.catwatch.backend.model.Project();
        project.setId(1);
        assertEquals(1, project.getId());
    }

    @Test
    public void testProjectGetLanguageList() {
        org.zalando.catwatch.backend.model.Project project = new org.zalando.catwatch.backend.model.Project();
        assertNotNull(project.getLanguageList());
    }

    @Test
    public void testProjectGetGitHubProjectId() {
        org.zalando.catwatch.backend.model.Project project = new org.zalando.catwatch.backend.model.Project();
        project.setGitHubProjectId(123456);
        assertEquals(123456, project.getGitHubProjectId());
    }

    // Repeat similar tests for other methods with lines to ensure full coverage

    @Test
    public void testQProjectInit() {
        org.zalando.catwatch.backend.model.QProject qProject = new org.zalando.catwatch.backend.model.QProject("project");
        assertNotNull(qProject);
    }

}
