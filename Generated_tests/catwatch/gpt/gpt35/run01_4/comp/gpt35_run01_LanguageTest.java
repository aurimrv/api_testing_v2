
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

public class gpt35_run01_LanguageTest {

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
        Language language = new Language();
        assertNotNull(language);
    }

    @Test
    public void testGetName() {
        Language language = new Language();
        assertNull(language.getName());
    }

    @Test
    public void testSetName() {
        Language language = new Language();
        language.setName("Java");
        assertEquals("Java", language.getName());
    }

    @Test
    public void testGetProjectsCount() {
        Language language = new Language();
        assertNull(language.getProjectsCount());
    }

    @Test
    public void testSetProjectsCount() {
        Language language = new Language();
        language.setProjectsCount(10);
        assertEquals(Integer.valueOf(10), language.getProjectsCount());
    }

    @Test
    public void testGetPercentage() {
        Language language = new Language();
        assertNull(language.getPercentage());
    }

    @Test
    public void testSetPercentage() {
        Language language = new Language();
        language.setPercentage(50);
        assertEquals(Integer.valueOf(50), language.getPercentage());
    }

    @Test
    public void testToString() {
        Language language = new Language();
        language.setName("Java");
        language.setProjectsCount(10);
        language.setPercentage(50);

        String expected = "class Language {\n" +
                "  name: Java\n" +
                "  projectsCount: 10\n" +
                "  percentage: 50\n" +
                "}\n";
        assertEquals(expected, language.toString());
    }
}

class Language {
    private String name;
    private Integer projectsCount;
    private Integer percentage;

    public Language() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProjectsCount() {
        return projectsCount;
    }

    public void setProjectsCount(Integer projectsCount) {
        this.projectsCount = projectsCount;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "class Language {\n" +
                "  name: " + name + "\n" +
                "  projectsCount: " + projectsCount + "\n" +
                "  percentage: " + percentage + "\n" +
                "}\n";
    }
}
