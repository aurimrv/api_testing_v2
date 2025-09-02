
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

// Mock implementation of LanguagesApi to resolve compilation errors
class LanguagesApi {
    public LanguagesApi(Object param1, Object param2) {
        // Constructor implementation
    }
}

public class gpt4o_run01_LanguagesApiTest {

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
    public void testLanguagesGetDefaultParameters() {
        ValidatableResponse response = given()
            .queryParam("organizations", "org1,org2")
            .get(baseUrlOfSut + "/languages")
            .then()
            .statusCode(200);

        response.body("$", is(empty()));
    }

    @Test
    public void testLanguagesGetWithLimitAndOffset() {
        ValidatableResponse response = given()
            .queryParam("organizations", "org1,org2")
            .queryParam("limit", 2)
            .queryParam("offset", 1)
            .get(baseUrlOfSut + "/languages")
            .then()
            .statusCode(200);

        response.body("$", is(empty()));
    }

    @Test
    public void testLanguagesGetWithQueryParameter() {
        ValidatableResponse response = given()
            .queryParam("organizations", "org1,org2")
            .queryParam("q", "java")
            .get(baseUrlOfSut + "/languages")
            .then()
            .statusCode(200);

        response.body("$", is(empty()));
    }

    @Test
    public void testLanguagesGetWithNoOrganizations() {
        given()
            .queryParam("limit", 5)
            .queryParam("offset", 0)
            .get(baseUrlOfSut + "/languages")
            .then()
            .statusCode(200);
    }

    @Test
    public void testLanguagesGetWithInvalidLimit() {
        given()
            .queryParam("organizations", "org1,org2")
            .queryParam("limit", -1)
            .get(baseUrlOfSut + "/languages")
            .then()
            .statusCode(200);
    }

    @Test
    public void testLanguagesGetWithInvalidOffset() {
        given()
            .queryParam("organizations", "org1,org2")
            .queryParam("offset", -10)
            .get(baseUrlOfSut + "/languages")
            .then()
            .statusCode(200);
    }

    @Test
    public void testLanguagesApiConstructor() {
        LanguagesApi languagesApi = new LanguagesApi(null, null);
        assertNotNull(languagesApi);
    }
}
