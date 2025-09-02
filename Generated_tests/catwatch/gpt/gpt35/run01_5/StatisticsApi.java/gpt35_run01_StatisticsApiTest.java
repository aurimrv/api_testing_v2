
@Test
public void test_statisticsProjectGet() {
    // Test for statisticsProjectGet method
    given()
        .queryParam("organizations", "org1,org2")
        .queryParam("startDateString", "2022-01-01")
        .queryParam("endDateString", "2022-12-31")
    .when()
        .get(baseUrlOfSut + "/statistics/projects")
    .then()
        .statusCode(200);
}

@Test
public void test_statisticsContributorGet() {
    // Test for statisticsContributorGet method
    given()
        .queryParam("organizations", "org1,org2")
        .queryParam("startDateString", "2022-01-01")
        .queryParam("endDateString", "2022-12-31")
    .when()
        .get(baseUrlOfSut + "/statistics/contributors")
    .then()
        .statusCode(200);
}

@Test
public void test_statisticsLanguagesGet() {
    // Test for statisticsLanguagesGet method
    given()
        .queryParam("organizations", "org1,org2")
        .queryParam("startDateString", "2022-01-01")
        .queryParam("endDateString", "2022-12-31")
    .when()
        .get(baseUrlOfSut + "/statistics/languages")
    .then()
        .statusCode(200);
}

@Test
public void test_statisticsProjectGet_Exception() {
    // Test for exception handling in statisticsProjectGet method
    given()
        .queryParam("organizations", "org1,org2")
        .queryParam("startDateString", "invalid_date")
        .queryParam("endDateString", "2022-12-31")
    .when()
        .get(baseUrlOfSut + "/statistics/projects")
    .then()
        .statusCode(400);
}

@Test
public void test_statisticsContributorGet_Exception() {
    // Test for exception handling in statisticsContributorGet method
    given()
        .queryParam("organizations", "org1,org2")
        .queryParam("startDateString", "2022-01-01")
        .queryParam("endDateString", "invalid_date")
    .when()
        .get(baseUrlOfSut + "/statistics/contributors")
    .then()
        .statusCode(400);
}

@Test
public void test_statisticsLanguagesGet_Exception() {
    // Test for exception handling in statisticsLanguagesGet method
    given()
        .queryParam("organizations", "org1,org2")
        .queryParam("startDateString", "2022-01-01")
        .queryParam("endDateString", "invalid_date")
    .when()
        .get(baseUrlOfSut + "/statistics/languages")
    .then()
        .statusCode(400);
}
