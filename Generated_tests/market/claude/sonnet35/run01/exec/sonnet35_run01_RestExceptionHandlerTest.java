package market;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;
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

public class sonnet35_run01_RestExceptionHandlerTest {

    private static final SutHandler controller = new em.embedded.market.EmbeddedEvoMasterController();
    private static String baseUrlOfSut;

    @BeforeAll
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

    @AfterAll
    public static void tearDown() {
        controller.stopSut();
    }

    @BeforeEach
    public void initTest() {
        controller.resetDatabase(Arrays.asList("USER_ROLE","CUSTOMER_ORDER","CART_ITEM","PRODUCT","CART","CONTACTS"));
        controller.resetStateOfSUT();
    }

    @Test
    public void testCustomNotValidException() {
        given()
            .contentType("application/json")
            .body("{\"email\":\"invalid_email\",\"password\":\"short\",\"name\":\"Test User\",\"phone\":\"+1 123 456 7890\",\"address\":\"123 Test St\"}")
        .when()
            .post(baseUrlOfSut + "/register")
        .then()
            .statusCode(406)
            .body("message", containsString("Argument validation error"))
            .body("entityType", equalTo(null))
            .body("fieldErrors", hasSize(greaterThan(0)));
    }

    @Test
    public void testResolveErrorMessage() {
        given()
            .contentType("application/json")
            .body("{\"email\":\"test@example.com\",\"password\":\"validpassword\",\"name\":\"Test User\",\"phone\":\"+1 123 456 7890\",\"address\":\"123 Test St\"}")
        .when()
            .post(baseUrlOfSut + "/register")
        .then()
            .statusCode(201);

        given()
            .contentType("application/json")
            .body("{\"email\":\"test@example.com\",\"password\":\"validpassword\",\"name\":\"Test User\",\"phone\":\"+1 123 456 7890\",\"address\":\"123 Test St\"}")
        .when()
            .post(baseUrlOfSut + "/register")
        .then()
            .statusCode(406)
            .body("fieldErrors.email", hasItem(not(emptyOrNullString())));
    }

    @Test
    public void testRestExceptionHandlerConstructor() {
        new market.rest.exception.RestExceptionHandler(null);
    }
}
