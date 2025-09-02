
import market.gpt35_run01_CustomerRestControllerTest.UserDTO; // Add this import statement

@Test
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
        .body("email", equalTo("test@test.com")) // Change is to equalTo
        .body("name", equalTo("Test User")) // Change is to equalTo
        .body("phone", equalTo("+1234567890")) // Change is to equalTo
        .body("address", equalTo("Test Address")); // Change is to equalTo

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
