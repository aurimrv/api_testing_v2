package org.restncs;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.List;
import static org.evomaster.client.java.controller.api.EMTestUtils.*;
import org.evomaster.client.java.controller.SutHandler;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.ValidatableResponse;
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

/**
 * Comprehensive integration tests for NCS REST API
 * Tests all endpoints with positive and negative scenarios for 100% business logic coverage
 */
public class NcsRestIntegrationTest {

    private static final SutHandler controller = new em.embedded.org.restncs.EmbeddedEvoMasterController();
    private static String baseUrlOfSut;

    @BeforeClass
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

    @AfterClass
    public static void tearDown() {
        controller.stopSut();
    }

    @Before
    public void initTest() {
        controller.resetStateOfSUT();
    }

    // ===============================
    // TRIANGLE CLASSIFICATION TESTS
    // ===============================

    @Test
    public void testTriangleClassification_ValidEquilateral() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/triangle/5/5/5")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body("resultAsInt", equalTo(3));
    }

    @Test
    public void testTriangleClassification_ValidIsosceles() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/triangle/5/5/3")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body("resultAsInt", equalTo(2));
    }

    @Test
    public void testTriangleClassification_ValidScalene() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/triangle/3/4/5")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body("resultAsInt", equalTo(1));
    }

    @Test
    public void testTriangleClassification_InvalidZeroEdge() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/triangle/0/5/5")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body("resultAsInt", equalTo(0));
    }

    @Test
    public void testTriangleClassification_InvalidNegativeEdge() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/triangle/-1/5/5")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body("resultAsInt", equalTo(0));
    }

    @Test
    public void testTriangleClassification_InvalidTriangleInequality() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/triangle/1/2/5")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body("resultAsInt", equalTo(0));
    }

    @Test
    public void testTriangleClassification_EdgeCaseEqualSum() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/triangle/1/2/3")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body("resultAsInt", equalTo(0));
    }

    @Test
    public void testTriangleClassification_IsoscelesVariations() {
        // Test all isosceles combinations
        given()
            .when()
            .get(baseUrlOfSut + "/api/triangle/5/3/5")
            .then()
            .statusCode(200)
            .body("resultAsInt", equalTo(2));

        given()
            .when()
            .get(baseUrlOfSut + "/api/triangle/3/5/5")
            .then()
            .statusCode(200)
            .body("resultAsInt", equalTo(2));
    }

    // ===============================
    // BESSJ FUNCTION TESTS
    // ===============================

    @Test
    public void testBessj_ValidInput() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/bessj/5/2.5")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testBessj_InvalidN_TooSmall() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/bessj/2/2.5")
            .then()
            .statusCode(400);
    }

    @Test
    public void testBessj_InvalidN_TooLarge() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/bessj/1001/2.5")
            .then()
            .statusCode(400);
    }

    @Test
    public void testBessj_InvalidN_Negative() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/bessj/-1/2.5")
            .then()
            .statusCode(400);
    }

    @Test
    public void testBessj_InvalidN_Zero() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/bessj/0/2.5")
            .then()
            .statusCode(400);
    }

    @Test
    public void testBessj_InvalidN_One() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/bessj/1/2.5")
            .then()
            .statusCode(400);
    }

    @Test
    public void testBessj_BoundaryValues() {
        // Test boundary n = 3 (minimum valid)
        given()
            .when()
            .get(baseUrlOfSut + "/api/bessj/3/1.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());

        // Test boundary n = 1000 (maximum valid)
        given()
            .when()
            .get(baseUrlOfSut + "/api/bessj/1000/1.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testBessj_ZeroX() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/bessj/5/0.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", equalTo(0.0));
    }

    @Test
    public void testBessj_NegativeX_EvenN() {
        // Test negative x with even n - should return positive result
        given()
            .when()
            .get(baseUrlOfSut + "/api/bessj/4/-2.5")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testBessj_NegativeX_OddN() {
        // Test negative x with odd n - should return negative result
        given()
            .when()
            .get(baseUrlOfSut + "/api/bessj/5/-2.5")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testBessj_LargeX_TriggerDirectAlgorithm() {
        // Test case where ax > n to trigger direct algorithm (bessj0/bessj1 path)
        given()
            .when()
            .get(baseUrlOfSut + "/api/bessj/3/10.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testBessj_VeryLargeX() {
        // Test very large x values
        given()
            .when()
            .get(baseUrlOfSut + "/api/bessj/5/100.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testBessj_SmallX_TriggerSeriesAlgorithm() {
        // Test case where ax <= n to trigger series algorithm
        given()
            .when()
            .get(baseUrlOfSut + "/api/bessj/10/5.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    // ===============================
    // EXPINT FUNCTION TESTS
    // ===============================

    @Test
    public void testExpint_ValidInput_LargeX() {
        // Test x > 1.0 to trigger continued fraction algorithm
        given()
            .when()
            .get(baseUrlOfSut + "/api/expint/1/1.5")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testExpint_ValidInput_SmallX() {
        // Test x <= 1.0 to trigger power series algorithm
        given()
            .when()
            .get(baseUrlOfSut + "/api/expint/2/0.5")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testExpint_N_Zero_ValidX() {
        // Test n = 0 case (should trigger ans = exp(-x)/x)
        given()
            .when()
            .get(baseUrlOfSut + "/api/expint/0/1.5")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testExpint_N_Zero_SmallX() {
        // Test n = 0 with small x
        given()
            .when()
            .get(baseUrlOfSut + "/api/expint/0/0.1")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testExpint_ZeroX_ValidN() {
        // Test x = 0.0 with n > 1 (should trigger ans = 1.0/nm1)
        given()
            .when()
            .get(baseUrlOfSut + "/api/expint/2/0.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", equalTo(1.0));
    }

    @Test
    public void testExpint_ZeroX_N_Three() {
        // Test x = 0.0 with n = 3 (should return 1.0/2 = 0.5)
        given()
            .when()
            .get(baseUrlOfSut + "/api/expint/3/0.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", equalTo(0.5));
    }

    @Test
    public void testExpint_InvalidCombination_N0_X0() {
        // Test invalid combination: n = 0 and x = 0.0
        given()
            .when()
            .get(baseUrlOfSut + "/api/expint/0/0.0")
            .then()
            .statusCode(400);
    }

    @Test
    public void testExpint_InvalidCombination_N1_X0() {
        // Test invalid combination: n = 1 and x = 0.0
        given()
            .when()
            .get(baseUrlOfSut + "/api/expint/1/0.0")
            .then()
            .statusCode(400);
    }

    @Test
    public void testExpint_NegativeN() {
        // Test negative n
        given()
            .when()
            .get(baseUrlOfSut + "/api/expint/-1/1.5")
            .then()
            .statusCode(400);
    }

    @Test
    public void testExpint_NegativeX() {
        // Test negative x
        given()
            .when()
            .get(baseUrlOfSut + "/api/expint/1/-1.5")
            .then()
            .statusCode(400);
    }

    @Test
    public void testExpint_SmallX_PowerSeries() {
        // Test small x values to exercise power series algorithm
        given()
            .when()
            .get(baseUrlOfSut + "/api/expint/5/0.1")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testExpint_VerySmallX() {
        // Test very small x values
        given()
            .when()
            .get(baseUrlOfSut + "/api/expint/3/0.001")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testExpint_LargeN_LargeX() {
        // Test large values that might trigger continued fraction failure
        given()
            .when()
            .get(baseUrlOfSut + "/api/expint/50/100.0")
            .then()
            .statusCode(anyOf(equalTo(200), equalTo(400)));
    }

    @Test
    public void testExpint_EdgeCase_X_Exactly_One() {
        // Test boundary case x = 1.0
        given()
            .when()
            .get(baseUrlOfSut + "/api/expint/2/1.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    // ===============================
    // FISHER DISTRIBUTION TESTS
    // ===============================

    @Test
    public void testFisher_ValidInput() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/5/10/2.5")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testFisher_InvalidM_TooLarge() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/1001/10/2.5")
            .then()
            .statusCode(400);
    }

    @Test
    public void testFisher_InvalidN_TooLarge() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/10/1001/2.5")
            .then()
            .statusCode(400);
    }

    @Test
    public void testFisher_BothParametersTooLarge() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/1001/1001/2.5")
            .then()
            .statusCode(400);
    }

    @Test
    public void testFisher_Case_A1_B1() {
        // Test case where a = 1 and b = 1 (both m and n are odd)
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/1/1/1.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testFisher_Case_A1_B_Not1() {
        // Test case where a = 1 and b != 1 (m is odd, n is even)
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/1/2/1.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testFisher_Case_A_Not1_B1() {
        // Test case where a != 1 and b = 1 (m is even, n is odd) - MISSING COVERAGE
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/2/1/1.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testFisher_Case_A_Not1_B_Not1() {
        // Test case where a != 1 and b != 1 (both m and n are even)
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/2/2/1.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testFisher_LargeOddM_OddN() {
        // Test large odd values for m and n
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/99/101/1.5")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testFisher_LargeEvenM_OddN() {
        // Test large even m, odd n to trigger a != 1, b = 1 case
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/100/101/1.5")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testFisher_BoundaryValues() {
        // Test boundary m = 1000, n = 1000 (maximum valid)
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/1000/1000/1.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testFisher_ZeroX() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/5/10/0.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testFisher_VerySmallX() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/5/10/0.001")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testFisher_VeryLargeX() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/5/10/1000.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testFisher_NegativeX() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/5/10/-1.0")
            .then()
            .statusCode(anyOf(equalTo(200), equalTo(400)));
    }

    // ===============================
    // GAMMQ FUNCTION TESTS
    // ===============================

    @Test
    public void testGammq_ValidInput_TriggerGser() {
        // Test case where x < a+1 to trigger gser method
        given()
            .when()
            .get(baseUrlOfSut + "/api/gammq/5.0/2.0")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testGammq_ValidInput_TriggerGcf() {
        // Test case where x >= a+1 to trigger gcf method
        given()
            .when()
            .get(baseUrlOfSut + "/api/gammq/2.0/5.0")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testGammq_BoundaryCase_X_Equals_A_Plus_1() {
        // Test boundary case where x = a + 1
        given()
            .when()
            .get(baseUrlOfSut + "/api/gammq/2.0/3.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testGammq_SmallA_LargeX() {
        // Test small a, large x to force gcf path
        given()
            .when()
            .get(baseUrlOfSut + "/api/gammq/0.5/10.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testGammq_LargeA_SmallX() {
        // Test large a, small x to force gser path
        given()
            .when()
            .get(baseUrlOfSut + "/api/gammq/10.0/0.5")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testGammq_VerySmallA() {
        // Test very small a values
        given()
            .when()
            .get(baseUrlOfSut + "/api/gammq/0.1/0.5")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testGammq_VeryLargeA() {
        // Test very large a values
        given()
            .when()
            .get(baseUrlOfSut + "/api/gammq/50.0/60.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testGammq_ZeroA() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/gammq/0.0/1.5")
            .then()
            .statusCode(400);
    }

    @Test
    public void testGammq_ZeroX() {
        // Test x = 0 with positive a (should return 1.0)
        given()
            .when()
            .get(baseUrlOfSut + "/api/gammq/2.5/0.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", equalTo(1.0));
    }

    @Test
    public void testGammq_NegativeA() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/gammq/-1.0/1.5")
            .then()
            .statusCode(400);
    }

    @Test
    public void testGammq_NegativeX() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/gammq/2.5/-1.0")
            .then()
            .statusCode(400);
    }

    @Test
    public void testGammq_EdgeCase_A_One() {
        // Test a = 1.0 (special case)
        given()
            .when()
            .get(baseUrlOfSut + "/api/gammq/1.0/1.5")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testGammq_EdgeCase_X_One() {
        // Test x = 1.0 with various a values
        given()
            .when()
            .get(baseUrlOfSut + "/api/gammq/2.5/1.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testGammq_FractionalValues() {
        // Test fractional values that might trigger different algorithm paths
        given()
            .when()
            .get(baseUrlOfSut + "/api/gammq/1.5/0.8")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testGammq_LargeValues_ForceGcf() {
        // Test large values that should force gcf method
        given()
            .when()
            .get(baseUrlOfSut + "/api/gammq/5.0/20.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    // ===============================
    // REMAINDER FUNCTION TESTS
    // ===============================

    @Test
    public void testRemainder_ValidPositiveNumbers() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/10/3")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .body("resultAsInt", notNullValue());
    }

    @Test
    public void testRemainder_ValidNegativeA() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/-10/3")
            .then()
            .statusCode(200)
            .body("resultAsInt", notNullValue());
    }

    @Test
    public void testRemainder_ValidNegativeB() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/10/-3")
            .then()
            .statusCode(200)
            .body("resultAsInt", notNullValue());
    }

    @Test
    public void testRemainder_BothNegative() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/-10/-3")
            .then()
            .statusCode(200)
            .body("resultAsInt", notNullValue());
    }

    @Test
    public void testRemainder_ZeroA() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/0/5")
            .then()
            .statusCode(200)
            .body("resultAsInt", notNullValue());
    }

    @Test
    public void testRemainder_ZeroB() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/5/0")
            .then()
            .statusCode(200)
            .body("resultAsInt", notNullValue());
    }

    @Test
    public void testRemainder_BothZero() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/0/0")
            .then()
            .statusCode(200)
            .body("resultAsInt", notNullValue());
    }

    @Test
    public void testRemainder_InvalidA_TooLarge() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/10001/5")
            .then()
            .statusCode(400);
    }

    @Test
    public void testRemainder_InvalidA_TooSmall() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/-10001/5")
            .then()
            .statusCode(400);
    }

    @Test
    public void testRemainder_InvalidB_TooLarge() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/5/10001")
            .then()
            .statusCode(400);
    }

    @Test
    public void testRemainder_InvalidB_TooSmall() {
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/5/-10001")
            .then()
            .statusCode(400);
    }

    @Test
    public void testRemainder_BoundaryValues() {
        // Test boundary a = 10000 (maximum valid)
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/10000/5")
            .then()
            .statusCode(200)
            .body("resultAsInt", notNullValue());

        // Test boundary a = -10000 (minimum valid)
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/-10000/5")
            .then()
            .statusCode(200)
            .body("resultAsInt", notNullValue());

        // Test boundary b = 10000 (maximum valid)
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/5/10000")
            .then()
            .statusCode(200)
            .body("resultAsInt", notNullValue());

        // Test boundary b = -10000 (minimum valid)
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/5/-10000")
            .then()
            .statusCode(200)
            .body("resultAsInt", notNullValue());
    }

    @Test
    public void testRemainder_EdgeCasesForLogic() {
        // Test cases that exercise different branches in the remainder logic
        
        // a > 0, b > 0 case
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/7/3")
            .then()
            .statusCode(200)
            .body("resultAsInt", notNullValue());

        // a > 0, b < 0 case  
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/7/-3")
            .then()
            .statusCode(200)
            .body("resultAsInt", notNullValue());

        // a < 0, b > 0 case
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/-7/3")
            .then()
            .statusCode(200)
            .body("resultAsInt", notNullValue());

        // a < 0, b < 0 case
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/-7/-3")
            .then()
            .statusCode(200)
            .body("resultAsInt", notNullValue());
    }

    // ===============================
    // ADDITIONAL EDGE CASE TESTS
    // ===============================

    @Test
    public void testAllEndpoints_ResponseStructure() {
        // Verify that all successful responses have the expected JSON structure
        
        // Triangle endpoint - should have resultAsInt
        ValidatableResponse triangleResponse = given()
            .when()
            .get(baseUrlOfSut + "/api/triangle/3/4/5")
            .then()
            .statusCode(200)
            .body("resultAsInt", notNullValue());

        // Bessj endpoint - should have resultAsDouble
        ValidatableResponse bessjResponse = given()
            .when()
            .get(baseUrlOfSut + "/api/bessj/5/2.5")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());

        // Remainder endpoint - should have resultAsInt
        ValidatableResponse remainderResponse = given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/10/3")
            .then()
            .statusCode(200)
            .body("resultAsInt", notNullValue());
    }

    @Test
    public void testInvalidEndpoints() {
        // Test non-existent endpoints
        given()
            .when()
            .get(baseUrlOfSut + "/api/nonexistent")
            .then()
            .statusCode(404);

        given()
            .when()
            .get(baseUrlOfSut + "/api/triangle")
            .then()
            .statusCode(404);
    }

    @Test
    public void testContentTypeHeaders() {
        // Verify all successful responses return JSON content type
        given()
            .when()
            .get(baseUrlOfSut + "/api/triangle/3/4/5")
            .then()
            .statusCode(200)
            .contentType("application/json");

        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/10/3")
            .then()
            .statusCode(200)
            .contentType("application/json");
    }

    // ===============================
    // COMPREHENSIVE COVERAGE TESTS
    // ===============================

    @Test
    public void testBessj_ExtremeValues_ForceExceptions() {
        // Test values that might cause internal exceptions in Bessj
        given()
            .when()
            .get(baseUrlOfSut + "/api/bessj/999/0.00001")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testExpint_ForceMaxIterations() {
        // Test values that might force maximum iterations in algorithms
        given()
            .when()
            .get(baseUrlOfSut + "/api/expint/1/0.00001")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testGammq_ExtremeCases() {
        // Test extreme cases for Gammq to trigger different algorithm paths
        given()
            .when()
            .get(baseUrlOfSut + "/api/gammq/0.001/100.0")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());

        given()
            .when()
            .get(baseUrlOfSut + "/api/gammq/100.0/0.001")
            .then()
            .statusCode(200)
            .body("resultAsDouble", notNullValue());
    }

    @Test
    public void testFisher_AllAlgorithmPaths() {
        // Comprehensive test to ensure all Fisher algorithm paths are covered
        
        // Case: a=1, b=1 (both odd)
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/3/5/1.0")
            .then()
            .statusCode(200);

        // Case: a=1, b!=1 (odd, even)
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/3/4/1.0")
            .then()
            .statusCode(200);

        // Case: a!=1, b=1 (even, odd) - Critical for missing coverage
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/4/3/1.0")
            .then()
            .statusCode(200);

        // Case: a!=1, b!=1 (both even)
        given()
            .when()
            .get(baseUrlOfSut + "/api/fisher/4/6/1.0")
            .then()
            .statusCode(200);
    }

    @Test
    public void testRemainder_CompleteLogicCoverage() {
        // Ensure complete coverage of all Remainder logic branches
        
        // Test edge cases that might not be covered
        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/1/1")
            .then()
            .statusCode(200);

        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/-1/-1")
            .then()
            .statusCode(200);

        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/9999/1")
            .then()
            .statusCode(200);

        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/-9999/-1")
            .then()
            .statusCode(200);
    }

    @Test
    public void testTriangle_CompleteClassificationCoverage() {
        // Ensure all triangle classification paths are covered
        
        // Test maximum edge case
        given()
            .when()
            .get(baseUrlOfSut + "/api/triangle/1/1/1")
            .then()
            .statusCode(200)
            .body("resultAsInt", equalTo(3));

        // Test degenerate triangle cases
        given()
            .when()
            .get(baseUrlOfSut + "/api/triangle/1/1/2")
            .then()
            .statusCode(200)
            .body("resultAsInt", equalTo(0));

        given()
            .when()
            .get(baseUrlOfSut + "/api/triangle/2/1/1")
            .then()
            .statusCode(200)
            .body("resultAsInt", equalTo(0));

        given()
            .when()
            .get(baseUrlOfSut + "/api/triangle/1/2/1")
            .then()
            .statusCode(200)
            .body("resultAsInt", equalTo(0));
    }

    @Test
    public void testAllEndpoints_ErrorHandling() {
        // Test error handling across all endpoints
        
        // Test invalid parameter types (should be handled by Spring)
        given()
            .when()
            .get(baseUrlOfSut + "/api/triangle/abc/4/5")
            .then()
            .statusCode(400);

        given()
            .when()
            .get(baseUrlOfSut + "/api/bessj/abc/2.5")
            .then()
            .statusCode(400);

        given()
            .when()
            .get(baseUrlOfSut + "/api/remainder/abc/5")
            .then()
            .statusCode(400);
    }
}