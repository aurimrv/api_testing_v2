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

public class sonnet35_run01_LanguagesApiTest {

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
    public void testLanguagesGetWithAllParameters() {
        given()
            .queryParam("organizations", "org1,org2")
            .queryParam("limit", 10)
            .queryParam("offset", 0)
            .queryParam("q", "Java")
        .when()
            .get(baseUrlOfSut + "/languages")
        .then()
            .statusCode(200)
            .body("", hasSize(0));
    }

    @Test
    public void testLanguagesGetWithRequiredParameterOnly() {
        given()
            .queryParam("organizations", "org1")
        .when()
            .get(baseUrlOfSut + "/languages")
        .then()
            .statusCode(200)
            .body("", hasSize(0));
    }

    @Test
    public void testLanguagesGetWithMissingRequiredParameter() {
        given()
        .when()
            .get(baseUrlOfSut + "/languages")
        .then()
            .statusCode(200);
    }

    @Test
    public void testLanguagesGetWithInvalidLimit() {
        given()
            .queryParam("organizations", "org1")
            .queryParam("limit", -1)
        .when()
            .get(baseUrlOfSut + "/languages")
        .then()
            .statusCode(500);
    }

    @Test
    public void testLanguagesGetWithInvalidOffset() {
        given()
            .queryParam("organizations", "org1")
            .queryParam("offset", -1)
        .when()
            .get(baseUrlOfSut + "/languages")
        .then()
            .statusCode(500);
    }

    @Test
    public void testLanguagesGetWithEmptyResult() {
        given()
            .queryParam("organizations", "non_existing_org")
        .when()
            .get(baseUrlOfSut + "/languages")
        .then()
            .statusCode(200)
            .body("", hasSize(0));
    }

    @Test
    public void testLanguagesGetResponseStructure() {
        given()
            .queryParam("organizations", "org1")
        .when()
            .get(baseUrlOfSut + "/languages")
        .then()
            .statusCode(200)
            .body("[0]", nullValue());
    }

    @Test
    public void testLanguagesGetWithSearchQuery() {
        given()
            .queryParam("organizations", "org1")
            .queryParam("q", "Java")
        .when()
            .get(baseUrlOfSut + "/languages")
        .then()
            .statusCode(200)
            .body("name", everyItem(containsString("Java")));
    }
}
