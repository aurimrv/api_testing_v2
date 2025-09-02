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

public class MarketApiIntegrationTest {

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

// ========== CustomerRestController Tests ==========

@Test
@Timeout(10)
public void testCustomerRegistration_Success() {
    String requestBody = "{\"email\":\"ivan.petrov@yandex.ru\",\"password\":\"petrov\",\"name\":\"Ivan Petrov\",\"phone\":\"+7 123 456 78 90\",\"address\":\"Riesstrasse 18\"}";

    given()
        .contentType("application/json")
        .accept("application/json")
        .body(requestBody)
    .when()
        .post(baseUrlOfSut + "/register")
    .then()
        .statusCode(201)
        .body("email", equalTo("ivan.petrov@yandex.ru"))
        .body("name", equalTo("Ivan Petrov"));
}

@Test
@Timeout(10)
public void testCustomerRegistration_InvalidEmail() {
    String requestBody = "{\"email\":\"invalid-email\",\"password\":\"petrov\",\"name\":\"Ivan Petrov\",\"phone\":\"+7 123 456 78 90\",\"address\":\"Riesstrasse 18\"}";

    given()
        .contentType("application/json")
        .accept("application/json")
        .body(requestBody)
    .when()
        .post(baseUrlOfSut + "/register")
    .then()
        .statusCode(400);
}

@Test
@Timeout(10)
public void testCustomerLogin_Success() {
    // First register the user
    testCustomerRegistration_Success();
    
    given()
        .auth().preemptive().basic("ivan.petrov@yandex.ru", "petrov")
        .accept("application/json")
    .when()
        .get(baseUrlOfSut + "/customer")
    .then()
        .statusCode(200)
        .body("email", equalTo("ivan.petrov@yandex.ru"))
        .body("name", equalTo("Ivan Petrov"));
}

@Test
@Timeout(10)
public void testCustomerLogin_InvalidCredentials() {
    given()
        .auth().preemptive().basic("ivan.petrov@yandex.ru", "wrongpassword")
        .accept("application/json")
    .when()
        .get(baseUrlOfSut + "/customer")
    .then()
        .statusCode(401);
}

// ========== ProductsRestController Tests ==========

@Test
@Timeout(10)
public void testGetAllProducts() {
    given()
        .accept("application/json")
    .when()
        .get(baseUrlOfSut + "/products")
    .then()
        .statusCode(200)
        .body("$", hasSize(greaterThanOrEqualTo(0)));
}

@Test
@Timeout(10)
public void testGetProductById() {
    // First get all products to get an ID
    Integer productId = given()
        .accept("application/json")
        .when()
            .get(baseUrlOfSut + "/products")
        .then()
            .extract().path("[0].productId");
    
    given()
        .accept("application/json")
    .when()
        .get(baseUrlOfSut + "/products/" + productId)
    .then()
        .statusCode(200)
        .body("productId", equalTo(productId));
}

// ========== CartRestController Tests ==========

@Test
@Timeout(10)
public void testAddItemToCart() {
    // Register and login first
    testCustomerRegistration_Success();
    
    // Get a product ID
    Integer productId = given()
        .accept("application/json")
        .when()
            .get(baseUrlOfSut + "/products")
        .then()
            .extract().path("[0].productId");
    
    // Add to cart
    given()
        .auth().preemptive().basic("ivan.petrov@yandex.ru", "petrov")
        .contentType("application/json")
        .accept("application/json")
        .body("{\"productId\":" + productId + ", \"quantity\":1}")
    .when()
        .put(baseUrlOfSut + "/customer/cart")
    .then()
        .statusCode(200)
        .body("cartItems", hasSize(greaterThan(0)));
}

// ========== OrdersRestController Tests ==========

@Test
@Timeout(10)
public void testCreateOrder() {
    // Setup: register, login and add item to cart
    testAddItemToCart();
    
    // Create order by paying
    given()
        .auth().preemptive().basic("ivan.petrov@yandex.ru", "petrov")
        .contentType("application/json")
        .accept("application/json")
        .body("{\"ccNumber\":\"1234567890123456\"}")
    .when()
        .post(baseUrlOfSut + "/customer/cart/pay")
    .then()
        .statusCode(201)
        .body("id", notNullValue());
}

// ========== ContactsRestController Tests ==========

@Test
@Timeout(10)
public void testGetContactInfo() {
    // Register and login first since contacts endpoint requires authentication
    testCustomerRegistration_Success();
    
    given()
        .auth().preemptive().basic("ivan.petrov@yandex.ru", "petrov")
        .accept("application/json")
    .when()
        .get(baseUrlOfSut + "/customer/contacts")
    .then()
        .statusCode(200)
        .body("phone", notNullValue())
        .body("address", notNullValue());
}

@Test
@Timeout(10)
public void testGetCart() {
    // Register and login first
    testCustomerRegistration_Success();
    
    given()
        .auth().preemptive().basic("ivan.petrov@yandex.ru", "petrov")
        .accept("application/json")
    .when()
        .get(baseUrlOfSut + "/customer/cart")
    .then()
        .statusCode(200)
        .body("cartItems", hasSize(0))
        .body("totalCost", equalTo(0.0));
}

@Test
@Timeout(10)
public void testClearCart() {
    // Setup: add item to cart first
    testAddItemToCart();
    
    given()
        .auth().preemptive().basic("ivan.petrov@yandex.ru", "petrov")
        .accept("application/json")
    .when()
        .delete(baseUrlOfSut + "/customer/cart")
    .then()
        .statusCode(200)
        .body("cartItems", hasSize(0));
}

@Test
@Timeout(10)
public void testGetOrders() {
    // Register and login first
    testCustomerRegistration_Success();
    
    given()
        .auth().preemptive().basic("ivan.petrov@yandex.ru", "petrov")
        .accept("application/json")
    .when()
        .get(baseUrlOfSut + "/customer/orders")
    .then()
        .statusCode(200)
        .body("$", hasSize(greaterThanOrEqualTo(0)));
}

@Test
@Timeout(10)
public void testUpdateContacts() {
    // Register and login first
    testCustomerRegistration_Success();
    
    String contactsBody = "{\"phone\":\"+7 987 654 32 10\",\"address\":\"New Address 123\"}";
    
    given()
        .auth().preemptive().basic("ivan.petrov@yandex.ru", "petrov")
        .contentType("application/json")
        .accept("application/json")
        .body(contactsBody)
    .when()
        .put(baseUrlOfSut + "/customer/contacts")
    .then()
        .statusCode(200)
        .body("phone", equalTo("+7 987 654 32 10"))
        .body("address", equalTo("New Address 123"));
}

@Test
@Timeout(10)
public void testSetDelivery() {
    // Register and login first
    testCustomerRegistration_Success();
    
    given()
        .auth().preemptive().basic("ivan.petrov@yandex.ru", "petrov")
        .accept("application/json")
        .param("included", true)
    .when()
        .put(baseUrlOfSut + "/customer/cart/delivery")
    .then()
        .statusCode(200)
        .body("deliveryIncluded", equalTo(true));
}

@Test
@Timeout(10)
public void testGetProductById_NotFound() {
    given()
        .accept("application/json")
    .when()
        .get(baseUrlOfSut + "/products/99999")
    .then()
        .statusCode(404);
}

@Test
@Timeout(10)
public void testCustomerRegistration_MissingFields() {
    String requestBody = "{\"email\":\"test@test.com\"}";

    given()
        .contentType("application/json")
        .accept("application/json")
        .body(requestBody)
    .when()
        .post(baseUrlOfSut + "/register")
    .then()
        .statusCode(400);
}

}