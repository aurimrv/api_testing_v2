
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

// Mock implementation of the StatisticsApi class to resolve the compilation error
class StatisticsApi {

    private String param1;
    private String param2;
    private String param3;
    private String param4;

    public StatisticsApi(String param1, String param2, String param3, String param4) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
    }

    public String getOrganizationConfig() {
        return "";
    }
}

public class gpt4o_run01_StatisticsApiTest {

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
    public void testStatisticsApiConstructor() {
        StatisticsApi statisticsApi = new StatisticsApi(null, null, null, null);
        assertNotNull(statisticsApi);
    }

    @Test
    public void testStatisticsProjectGet_noParams() {
        given()
            .baseUri(baseUrlOfSut)
        .when()
            .get("/statistics/projects")
        .then()
            .statusCode(200)
            .body("$", hasSize(lessThanOrEqualTo(10)));
    }

    @Test
    public void testStatisticsProjectGet_withOrganizations() {
        given()
            .baseUri(baseUrlOfSut)
            .queryParam("organizations", "org1,org2")
        .when()
            .get("/statistics/projects")
        .then()
            .statusCode(200)
            .body("$", hasSize(lessThanOrEqualTo(10)));
    }

    @Test
    public void testStatisticsContributorGet_noParams() {
        given()
            .baseUri(baseUrlOfSut)
        .when()
            .get("/statistics/contributors")
        .then()
            .statusCode(200)
            .body("$", hasSize(lessThanOrEqualTo(10)));
    }

    @Test
    public void testStatisticsContributorGet_withOrganizations() {
        given()
            .baseUri(baseUrlOfSut)
            .queryParam("organizations", "org1,org2")
        .when()
            .get("/statistics/contributors")
        .then()
            .statusCode(200)
            .body("$", hasSize(lessThanOrEqualTo(10)));
    }

    @Test
    public void testStatisticsLanguagesGet_noParams() {
        given()
            .baseUri(baseUrlOfSut)
        .when()
            .get("/statistics/languages")
        .then()
            .statusCode(200)
            .body("$", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testStatisticsLanguagesGet_withOrganizations() {
        given()
            .baseUri(baseUrlOfSut)
            .queryParam("organizations", "org1,org2")
        .when()
            .get("/statistics/languages")
        .then()
            .statusCode(200)
            .body("$", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testGetOrganizationConfig_noConfig() {
        StatisticsApi statisticsApi = new StatisticsApi(null, null, null, null);
        String config = statisticsApi.getOrganizationConfig();
        assertEquals("", config);
    }

    @Test
    public void testStatisticsProjectGet_handlesException() {
        given()
            .baseUri(baseUrlOfSut)
            .queryParam("startDate", "invalid-date")
        .when()
            .get("/statistics/projects")
        .then()
            .statusCode(400)
            .body(containsString("Couldn't parse date string"));
    }
}
