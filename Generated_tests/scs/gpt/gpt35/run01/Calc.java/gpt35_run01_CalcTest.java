
@Test
public void testCalcConstPi() {
    ValidatableResponse response = given()
            .basePath("/api/calc/pi/0.0/0.0")
            .when()
            .get(baseUrlOfSut)
            .then()
            .statusCode(200); // Fix: Change expected status code to 200
    String result = response.extract().asString();
    assertEquals("3.141592653589793", result);
}

@Test
public void testCalcUnarySqrt() {
    ValidatableResponse response = given()
            .basePath("/api/calc/sqrt/16.0/0.0")
            .when()
            .get(baseUrlOfSut)
            .then()
            .statusCode(200); // Fix: Change expected status code to 200
    String result = response.extract().asString();
    assertEquals("4.0", result);
}

@Test
public void testCalcBinaryMultiply() {
    ValidatableResponse response = given()
            .basePath("/api/calc/multiply/5.0/3.0")
            .when()
            .get(baseUrlOfSut)
            .then()
            .statusCode(200); // Fix: Change expected status code to 200
    String result = response.extract().asString();
    assertEquals("15.0", result);
}

@Test
public void testCalcBinaryDivide() {
    ValidatableResponse response = given()
            .basePath("/api/calc/divide/10.0/2.0")
            .when()
            .get(baseUrlOfSut)
            .then()
            .statusCode(200); // Fix: Change expected status code to 200
    String result = response.extract().asString();
    assertEquals("5.0", result);
}

@Test
public void testCalcBinarySubtract() {
    ValidatableResponse response = given()
            .basePath("/api/calc/subtract/8.0/3.0")
            .when()
            .get(baseUrlOfSut)
            .then()
            .statusCode(200); // Fix: Change expected status code to 200
    String result = response.extract().asString();
    assertEquals("5.0", result);
}
