
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

public class gpt4o_run01_ContributorsApiTest {

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
            .queryParam("organizations", "test-org")
            .queryParam("limit", 5)
            .queryParam("offset", 0)
            .queryParam("start_date", "2023-01-01")
            .queryParam("end_date", "2023-12-31")
            .queryParam("sortBy", "-commits")
            .queryParam("q", "test")
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(200)
            .body("$", hasSize(lessThanOrEqualTo(5)));
    }

    @Test
    public void testContributorsGetWithNoStartDateAndEndDate() {
        given()
            .queryParam("organizations", "test-org")
            .queryParam("limit", 5)
            .queryParam("offset", 0)
            .queryParam("sortBy", "-commits")
            .queryParam("q", "test")
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(200)
            .body("$", hasSize(lessThanOrEqualTo(5)));
    }

    @Test
    public void testContributorsGetInvalidDates() {
        given()
            .queryParam("organizations", "test-org")
            .queryParam("start_date", "2023-12-31")
            .queryParam("end_date", "2023-01-01")
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(500)
            .body(containsString("startDate must be before endDate"));
    }

    @Test
    public void testContributorsGetUnsupportedConfiguration() {
        given()
            .queryParam("organizations", "test-org")
            .queryParam("start_date", "2023-01-01")
            .queryParam("end_date", "2023-12-31")
            .queryParam("q", "unsupported")
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(500)
            .body(containsString("this parameter configuration is not implemented yet"));
    }

    @Test
    public void testContributorsGetDefaultLimit() {
        given()
            .queryParam("organizations", "test-org")
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(200)
            .body("$", hasSize(lessThanOrEqualTo(5)));
    }

    @Test
    public void testSublistBoundaryCases() {
        given()
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(200)
            .body("$", notNullValue());
    }

    @Test
    public void testContributorsApiConstructorExecution() {
        assertNotNull(new org.zalando.catwatch.backend.web.ContributorsApi(null, null));
    }

    @Test
    public void testValidateInvalidLimit() {
        given()
            .queryParam("organizations", "test-org")
            .queryParam("limit", -1)
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(500)
            .body(containsString("limit must be greater than zero"));
    }

    @Test
    public void testValidateInvalidOffset() {
        given()
            .queryParam("organizations", "test-org")
            .queryParam("offset", -1)
        .when()
            .get(baseUrlOfSut + "/contributors")
        .then()
            .statusCode(500)
            .body(containsString("offset must be greater than zero"));
    }
}
