
package org.zalando.catwatch.backend;

import  org.junit.AfterClass;
import  org.junit.BeforeClass;
import  org.junit.Before;
import  org.junit.Test;
import static org.junit.Assert.*;
import  java.util.Map;
import  java.util.List;
import static org.evomaster.client.java.controller.api.EMTestUtils.*;
import  org.evomaster.client.java.controller.SutHandler;
import  io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import  io.restassured.response.ValidatableResponse;
import static org.evomaster.client.java.sql.dsl.SqlDsl.sql;
import  org.evomaster.client.java.controller.api.dto.database.operations.InsertionResultsDto;
import  org.evomaster.client.java.controller.api.dto.database.operations.InsertionDto;
import static org.hamcrest.Matchers.*;
import  io.restassured.config.JsonConfig;
import  io.restassured.path.json.config.JsonPathConfig;
import static io.restassured.config.RedirectConfig.redirectConfig;
import static org.evomaster.client.java.controller.contentMatchers.NumberMatcher.*;
import static org.evomaster.client.java.controller.contentMatchers.StringMatcher.*;
import static org.evomaster.client.java.controller.contentMatchers.SubStringMatcher.*;
import static org.evomaster.client.java.controller.expect.ExpectationHandler.expectationHandler;
import  org.evomaster.client.java.controller.expect.ExpectationHandler;
import  io.restassured.path.json.JsonPath;
import  java.util.Arrays;

public class gpt35_run01_StatisticsTest {

    private static final SutHandler controller = new em.embedded.org.zalando.EmbeddedEvoMasterController();
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
        controller.resetDatabase(Arrays.asList("CONTRIBUTOR"));
        controller.resetStateOfSUT();
    }

    @Test
    public void test_QStatistics_clinit_lines_21_23() {
        // Test coverage for <clinit> method lines 21, 23 in QStatistics class
        // Add test logic here

    }

    @Test
    public void test_QStatistics_init_lines_25_27_29_31_33_37_39_41_43_45_47_49_52_53_56_57_60_61_64_65_68_69_70() {
        // Test coverage for <init> method lines 25, 27, 29, 31, 33, 37, 39, 41, 43, 45, 47, 49, 52, 53, 56, 57, 60, 61, 64,
        // 65, 68, 69, 70 in QStatistics class
        // Add test logic here
    }

    @Test
    public void test_Statistics_init_lines_19_20_21_22_23_24_25_26_27_28_29_30_37_38_39() {
        // Test coverage for <init> method lines 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 37, 38, 39 in Statistics class
        // Add test logic here
    }

    @Test
    public void test_Statistics_getId_line_48() {
        // Test coverage for getId method line 48 in Statistics class
        // Add test logic here
    }

    @Test
    public void test_Statistics_getSnapshotDate_line_210() {
        // Test coverage for getSnapshotDate method line 210 in Statistics class
        // Add test logic here
    }

    @Test
    public void test_Statistics_setSnapshotDate_line_215() {
        // Test coverage for setSnapshotDate method line 215 in Statistics class
        // Add test logic here
    }

    @Test
    public void test_Statistics_toString_lines_222_223_224_225_226_227_228_229_230_231_232_233_234_235_236_237() {
        // Test coverage for toString method lines 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237 in Statistics class
        // Add test logic here
    }
}
