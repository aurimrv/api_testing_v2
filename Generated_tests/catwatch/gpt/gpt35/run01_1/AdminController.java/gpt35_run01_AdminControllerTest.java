
@Test
public void testInitMethod() {
    ValidatableResponse response = given()
        .when()
        .get(baseUrlOfSut + "/init")
        .then()
        .statusCode(200);
    // Add specific assertions as needed
}

@Test
public void testDeleteAllMethod() {
    ValidatableResponse response = given()
        .when()
        .get(baseUrlOfSut + "/delete")
        .then()
        .statusCode(200);
    // Add specific assertions as needed
}
