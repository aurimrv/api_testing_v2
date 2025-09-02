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

public class sonnet35_run01_CartRestControllerTest {

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
    public void testAddItem() {
        // Register a user
        given()
            .contentType("application/json")
            .body("{\"email\":\"ivan.petrov@yandex.ru\",\"password\":\"petrov\",\"name\":\"Ivan Petrov\",\"phone\":\"+7 123 456 78 90\",\"address\":\"Riesstrasse 18\"}")
            .post(baseUrlOfSut + "/register")
            .then()
            .statusCode(406);

        // Login
        String cookie = given()
            .contentType("application/x-www-form-urlencoded")
            .formParam("username", "ivan.petrov@yandex.ru")
            .formParam("password", "petrov")
            .post(baseUrlOfSut + "/login")
            .then()
            .statusCode(404)
            .extract().cookie("JSESSIONID");

        // Add item to cart
        given()
            .contentType("application/json")
            .cookie("JSESSIONID", cookie)
            .body("{\"productId\":1,\"quantity\":2}")
            .put(baseUrlOfSut + "/customer/cart")
            .then()
            .statusCode(200)
            .body("cartItems", hasSize(1))
            .body("cartItems[0].productId", equalTo(1))
            .body("cartItems[0].quantity", equalTo(2));
    }

    @Test
    public void testPayByCard() {
        // Register a user
        given()
            .contentType("application/json")
            .body("{\"email\":\"ivan.petrov@yandex.ru\",\"password\":\"petrov\",\"name\":\"Ivan Petrov\",\"phone\":\"+7 123 456 78 90\",\"address\":\"Riesstrasse 18\"}")
            .post(baseUrlOfSut + "/register")
            .then()
            .statusCode(406);

        // Login
        String cookie = given()
            .contentType("application/x-www-form-urlencoded")
            .formParam("username", "ivan.petrov@yandex.ru")
            .formParam("password", "petrov")
            .post(baseUrlOfSut + "/login")
            .then()
            .statusCode(404)
            .extract().cookie("JSESSIONID");

        // Add item to cart
        given()
            .contentType("application/json")
            .cookie("JSESSIONID", cookie)
            .body("{\"productId\":1,\"quantity\":2}")
            .put(baseUrlOfSut + "/customer/cart")
            .then()
            .statusCode(200);

        // Pay by card
        given()
            .contentType("application/json")
            .cookie("JSESSIONID", cookie)
            .body("{\"ccNumber\":\"1234567890123456\"}")
            .post(baseUrlOfSut + "/customer/cart/pay")
            .then()
            .statusCode(406)
            .body("payed", equalTo(true))
            .body("executed", equalTo(false));
    }

    @Test
    public void testPayByCardWithEmptyCart() {
        // Register a user
        given()
            .contentType("application/json")
            .body("{\"email\":\"ivan.petrov@yandex.ru\",\"password\":\"petrov\",\"name\":\"Ivan Petrov\",\"phone\":\"+7 123 456 78 90\",\"address\":\"Riesstrasse 18\"}")
            .post(baseUrlOfSut + "/register")
            .then()
            .statusCode(406);

        // Login
        String cookie = given()
            .contentType("application/x-www-form-urlencoded")
            .formParam("username", "ivan.petrov@yandex.ru")
            .formParam("password", "petrov")
            .post(baseUrlOfSut + "/login")
            .then()
            .statusCode(404)
            .extract().cookie("JSESSIONID");

        // Pay by card with empty cart
        given()
            .contentType("application/json")
            .cookie("JSESSIONID", cookie)
            .body("{\"ccNumber\":\"1234567890123456\"}")
            .post(baseUrlOfSut + "/customer/cart/pay")
            .then()
            .statusCode(400);
    }

    @Test
    public void testCartRestControllerConstructor() {
        // This test case is to cover the constructor of CartRestController
        // Since it's not possible to directly instantiate the controller in a test,
        // we'll verify that the controller is working by making a request that uses it

        // Register a user
        given()
            .contentType("application/json")
            .body("{\"email\":\"ivan.petrov@yandex.ru\",\"password\":\"petrov\",\"name\":\"Ivan Petrov\",\"phone\":\"+7 123 456 78 90\",\"address\":\"Riesstrasse 18\"}")
            .post(baseUrlOfSut + "/register")
            .then()
            .statusCode(406);

        // Login
        String cookie = given()
            .contentType("application/x-www-form-urlencoded")
            .formParam("username", "ivan.petrov@yandex.ru")
            .formParam("password", "petrov")
            .post(baseUrlOfSut + "/login")
            .then()
            .statusCode(404)
            .extract().cookie("JSESSIONID");

        // Get cart (this will use the CartRestController)
        given()
            .cookie("JSESSIONID", cookie)
            .get(baseUrlOfSut + "/customer/cart")
            .then()
            .statusCode(200)
            .body("user", equalTo("ivan.petrov@yandex.ru"));
    }
}
