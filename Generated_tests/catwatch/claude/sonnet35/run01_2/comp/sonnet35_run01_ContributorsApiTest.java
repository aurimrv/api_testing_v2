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

public class sonnet35_run01_ContributorsApiTest {

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
    public void testContributorsGetWithValidParameters() {
        given()
            .queryParam("organizations", "org1,org2")
            .queryParam("limit", 10)
            .queryParam("offset", 0)
            .queryParam("start_date", "2023-01-01")
            .queryParam("end_date", "2023-12-31")
            .queryParam("sortBy", "organizationalCommitsCount")
            .queryParam("q", "test")
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testContributorsGetWithInvalidOrganizations() {
        given()
            .queryParam("organizations", "")
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(400);
    }

    @Test
    public void testContributorsGetWithInvalidLimit() {
        given()
            .queryParam("organizations", "org1")
            .queryParam("limit", -1)
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(400);
    }

    @Test
    public void testContributorsGetWithInvalidOffset() {
        given()
            .queryParam("organizations", "org1")
            .queryParam("offset", -1)
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(400);
    }

    @Test
    public void testContributorsGetWithInvalidDateRange() {
        given()
            .queryParam("organizations", "org1")
            .queryParam("start_date", "2023-12-31")
            .queryParam("end_date", "2023-01-01")
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(400);
    }

    @Test
    public void testContributorsGetWithInvalidSortBy() {
        given()
            .queryParam("organizations", "org1")
            .queryParam("sortBy", "invalidField")
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(400);
    }

    @Test
    public void testContributorsGetNoTimeSpan() {
        given()
            .queryParam("organizations", "org1")
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testContributorsGetTimeSpan() {
        given()
            .queryParam("organizations", "org1")
            .queryParam("start_date", "2023-01-01")
            .queryParam("end_date", "2023-12-31")
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testContributorsGetWithUnsupportedOperation() {
        given()
            .queryParam("organizations", "org1")
            .queryParam("start_date", "2023-01-01")
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(500)
            .body(containsString("UnsupportedOperationException"));
    }

    @Test
    public void testSublist() {
        // This method is private, so we can't test it directly.
        // We can test it indirectly through the public API
        given()
            .queryParam("organizations", "org1")
            .queryParam("limit", 5)
            .queryParam("offset", 2)
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(200)
            .body("size()", lessThanOrEqualTo(5));
    }

    @Test
    public void testValidate() {
        // This method is private, so we can't test it directly.
        // We can test it indirectly through the public API
        given()
            .queryParam("organizations", "org1")
            .queryParam("limit", -1)
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(400);
    }

    @Test
    public void testSortBy() {
        given()
            .queryParam("organizations", "org1")
            .queryParam("sortBy", "-organizationalCommitsCount")
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(200)
            .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testConstructor() {
        // We can't directly test the constructor, but we can ensure the class can be instantiated
        // by calling a method that uses it
        given()
            .queryParam("organizations", "org1")
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(200);
    }
}
