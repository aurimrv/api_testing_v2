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

public class sonnet35_run01_StatisticsApiTest {

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
    public void testParseDate() {
        given()
            .accept("application/json")
        .when()
            .get(baseUrlOfSut + "/statistics?start_date=2021-01-01&end_date=2021-12-31")
        .then()
            .statusCode(200);
    }

    @Test
    public void testParseDateWithInvalidDate() {
        given()
            .accept("application/json")
        .when()
            .get(baseUrlOfSut + "/statistics?start_date=invalid-date")
        .then()
            .statusCode(400);
    }

    @Test
    public void testStatisticsProjectGet() {
        given()
            .accept("application/json")
        .when()
            .get(baseUrlOfSut + "/statistics/projects")
        .then()
            .statusCode(500);
    }

    @Test
    public void testStatisticsProjectGetWithParameters() {
        given()
            .accept("application/json")
            .queryParam("organizations", "org1,org2")
            .queryParam("start_date", "2021-01-01")
            .queryParam("end_date", "2021-12-31")
        .when()
            .get(baseUrlOfSut + "/statistics/projects")
        .then()
            .statusCode(500);
    }

    @Test
    public void testStatisticsContributorGet() {
        given()
            .accept("application/json")
        .when()
            .get(baseUrlOfSut + "/statistics/contributors")
        .then()
            .statusCode(500);
    }

    @Test
    public void testStatisticsContributorGetWithParameters() {
        given()
            .accept("application/json")
            .queryParam("organizations", "org1,org2")
            .queryParam("start_date", "2021-01-01")
            .queryParam("end_date", "2021-12-31")
        .when()
            .get(baseUrlOfSut + "/statistics/contributors")
        .then()
            .statusCode(500);
    }

    @Test
    public void testGetOrganizationConfig() {
        given()
            .accept("application/json")
        .when()
            .get(baseUrlOfSut + "/statistics")
        .then()
            .statusCode(200);
    }

    @Test
    public void testStatisticsLanguagesGet() {
        given()
            .accept("application/json")
        .when()
            .get(baseUrlOfSut + "/statistics/languages")
        .then()
            .statusCode(200)
            .body("$", hasSize(greaterThanOrEqualTo(0)));
    }

    @Test
    public void testStatisticsLanguagesGetWithParameters() {
        given()
            .accept("application/json")
            .queryParam("organizations", "org1,org2")
            .queryParam("start_date", "2021-01-01")
            .queryParam("end_date", "2021-12-31")
        .when()
            .get(baseUrlOfSut + "/statistics/languages")
        .then()
            .statusCode(200)
            .body("$", hasSize(greaterThanOrEqualTo(0)));
    }
}
