
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

public class gpt4o_run01_AdminControllerTest {

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
    public void testAdminControllerConstructor() {
        // Ensures <init> method coverage
        assertNotNull(new org.zalando.catwatch.backend.web.admin.AdminController(null, null, null));
    }

    @Test
    public void testInitEndpoint() {
        // Ensures /init endpoint coverage, including exception handling
        given()
            .baseUri(baseUrlOfSut)
        .when()
            .get("/init")
        .then()
            .statusCode(403)
            .body(containsString("This endpoint is deactivated."));
    }

    @Test
    public void testDeleteAllEndpoint() {
        // Ensures /delete endpoint coverage, including exception handling
        given()
            .baseUri(baseUrlOfSut)
        .when()
            .get("/delete")
        .then()
            .statusCode(403)
            .body(containsString("This endpoint is deactivated."));
    }

    @Test
    public void testHandleException() {
        // Directly tests the exception handler
        given()
            .baseUri(baseUrlOfSut)
        .when()
            .get("/init") // Any endpoint that throws UnsupportedOperationException
        .then()
            .statusCode(403)
            .body(containsString("This endpoint is deactivated."));
    }
}
