
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

public class gpt35_run01_BuilderUtilTest {

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
    public void testBuilderUtilConstructorIsExecuted() {
        new org.zalando.catwatch.backend.repo.builder.BuilderUtil();
    }

    @Test
    public void testBuilderUtilClinitIsExecuted() {
        assertNotNull(org.zalando.catwatch.backend.repo.builder.BuilderUtil.class);
    }

    @Test
    public void testBuilderUtilRandom() {
        int result = org.zalando.catwatch.backend.repo.builder.BuilderUtil.random(1, 10);
        assertTrue(result >= 1 && result < 10);
    }

    @Test
    public void testBuilderUtilRandomDate() {
        assertNotNull(org.zalando.catwatch.backend.repo.builder.BuilderUtil.randomDate());
    }

    @Test
    public void testBuilderUtilRandomLanguage() {
        String language = org.zalando.catwatch.backend.repo.builder.BuilderUtil.randomLanguage();
        List<String> langs = Arrays.asList("Java", "JS", "HTML5", "CSS", "Python", "C++", "Go", "Scala", "Groovy",
                "C#", "Clojure", "VB", "ObjectiveC");
        assertTrue(langs.contains(language));
    }

    @Test
    public void testBuilderUtilRandomProjectName() {
        String projectName = org.zalando.catwatch.backend.repo.builder.BuilderUtil.randomProjectName();
        assertNotNull(projectName);
    }

    @Test
    public void testBuilderUtilFreshId() {
        long id = org.zalando.catwatch.backend.repo.builder.BuilderUtil.freshId();
        assertTrue(id > 0);
    }

}
