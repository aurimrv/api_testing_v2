
@Test
public void testGetByAlphaList() {
    given()
            .when()
            .get(baseUrlOfSut + "/v2/alpha/")
            .then()
            .statusCode(200);
}

@Test
public void testGetByCallingCode() {
    given()
            .pathParam("callingcode", "123")
            .when()
            .get(baseUrlOfSut + "/v2/callingcode/{callingcode}")
            .then()
            .statusCode(200);
}

@Test
public void testDoPOST() {
    given()
            .when()
            .post(baseUrlOfSut + "/v2/")
            .then()
            .statusCode(201);
}
