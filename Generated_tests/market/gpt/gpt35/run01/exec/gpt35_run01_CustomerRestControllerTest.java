
package market;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Disabled;
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

public class gpt35_run01_CustomerRestControllerTest {

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
    @Disabled
    public void testCreateCustomer() {
        // Test case for successful customer creation
        UserDTO user = new UserDTO("test@test.com", "Test User", "password123", "+1234567890", "Test Address");
        given()
            .contentType("application/json")
            .body(user)
        .when()
            .post(baseUrlOfSut + "/register")
        .then()
            .statusCode(201)
            .body("email", is("test@test.com"))
            .body("name", is("Test User"))
            .body("phone", is("+1234567890"))
            .body("address", is("Test Address"));

        // Test case for EmailExistsException
        UserDTO existingUser = new UserDTO("ivan.petrov@yandex.ru", "Ivan Petrov", "petrov", "+7 123 456 78 90", "Riesstrasse 18");
        given()
            .contentType("application/json")
            .body(existingUser)
        .when()
            .post(baseUrlOfSut + "/register")
        .then()
            .statusCode(409); // Assuming 409 Conflict is returned for EmailExistsException
    }
    
    public static class UserDTO {
        private String email;
        private String name;
        private String password;
        private String phone;
        private String address;

        public UserDTO(String email, String name, String password, String phone, String address) {
            this.email = email;
            this.name = name;
            this.password = password;
            this.phone = phone;
            this.address = address;
        }

        // Getters and setters can be added if needed
    }
}
