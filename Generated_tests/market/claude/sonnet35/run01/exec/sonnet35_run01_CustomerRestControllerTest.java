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

public class sonnet35_run01_CustomerRestControllerTest {

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
    public void testCreateCustomerSuccess() {
        String email = "ivan.petrov@yandex.ru";
        String password = "petrov";
        String name = "Ivan Petrov";
        String phone = "+7 123 456 78 90";
        String address = "Riesstrasse 18";

        given()
            .contentType("application/json")
            .body("{\"email\":\"" + email + "\",\"password\":\"" + password + "\",\"name\":\"" + name + "\",\"phone\":\"" + phone + "\",\"address\":\"" + address + "\"}")
        .when()
            .post(baseUrlOfSut + "/register")
        .then()
            .statusCode(500)
            .body("email", equalTo(email))
            .body("name", equalTo(name))
            .body("phone", equalTo(phone))
            .body("address", equalTo(address));
    }

    @Test
    public void testCreateCustomerInvalidEmail() {
        given()
            .contentType("application/json")
            .body("{\"email\":\"invalid-email\",\"password\":\"password\",\"name\":\"Test User\",\"phone\":\"+1 234 567 8901\",\"address\":\"Test Address\"}")
        .when()
            .post(baseUrlOfSut + "/register")
        .then()
            .statusCode(406);
    }

    @Test
    public void testCreateCustomerEmptyPassword() {
        given()
            .contentType("application/json")
            .body("{\"email\":\"test@example.com\",\"password\":\"\",\"name\":\"Test User\",\"phone\":\"+1 234 567 8901\",\"address\":\"Test Address\"}")
        .when()
            .post(baseUrlOfSut + "/register")
        .then()
            .statusCode(406);
    }

    @Test
    public void testCreateCustomerDuplicateEmail() {
        // First, create a user
        given()
            .contentType("application/json")
            .body("{\"email\":\"duplicate@example.com\",\"password\":\"password\",\"name\":\"Test User\",\"phone\":\"+1 234 567 8901\",\"address\":\"Test Address\"}")
        .when()
            .post(baseUrlOfSut + "/register")
        .then()
            .statusCode(500);

        // Try to create another user with the same email
        given()
            .contentType("application/json")
            .body("{\"email\":\"duplicate@example.com\",\"password\":\"password2\",\"name\":\"Test User 2\",\"phone\":\"+1 987 654 3210\",\"address\":\"Test Address 2\"}")
        .when()
            .post(baseUrlOfSut + "/register")
        .then()
            .statusCode(406);
    }

    @Test
    public void testCreateCustomerInvalidPhone() {
        given()
            .contentType("application/json")
            .body("{\"email\":\"test@example.com\",\"password\":\"password\",\"name\":\"Test User\",\"phone\":\"invalid-phone\",\"address\":\"Test Address\"}")
        .when()
            .post(baseUrlOfSut + "/register")
        .then()
            .statusCode(406);
    }
}
