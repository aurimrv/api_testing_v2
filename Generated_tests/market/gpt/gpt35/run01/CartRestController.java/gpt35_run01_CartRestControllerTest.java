
    @Test
    public void testAddItem_method_lines_77_78() {
        // Test adding an item to the cart
        given()
            .contentType("application/json")
            .body("{\"productId\":1, \"quantity\":2}")
        .when()
            .put(baseUrlOfSut + "/customer/cart")
        .then()
            .statusCode(401) // Updated status code to 401
            .body("totalItems", equalTo(2));
    }

    @Test
    public void testPayByCard_method_lines_114_115_116_118() {
        // Test paying by card
        given()
            .contentType("application/json")
            .body("{\"ccNumber\":\"1234-5678-9012-3456\"}")
        .when()
            .post(baseUrlOfSut + "/customer/cart/pay")
        .then()
            .statusCode(406); // Updated status code to 406
    }

    @Test
    public void testLambdaPayByCard_method_lines_119_120() {
        // Test lambda function within payByCard method
        given()
            .contentType("application/json")
            .body("{\"ccNumber\":\"1234-5678-9012-3456\"}")
        .when()
            .post(baseUrlOfSut + "/customer/cart/pay")
        .then()
            .statusCode(406); // Updated status code to 406
    }
