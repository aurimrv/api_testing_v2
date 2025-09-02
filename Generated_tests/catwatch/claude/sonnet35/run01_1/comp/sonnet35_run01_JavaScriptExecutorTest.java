package org.zalando.catwatch.backend;

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

import org.zalando.catwatch.backend.util.JavaScriptExecutor;

public class sonnet35_run01_JavaScriptExecutorTest {

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
    public void testNewExecutor() {
        JavaScriptExecutor executor = JavaScriptExecutor.newExecutor("result.value = 'test'");
        assertNotNull(executor);
    }

    @Test
    public void testBind() {
        JavaScriptExecutor executor = JavaScriptExecutor.newExecutor("result.value = x");
        executor.bind("x", "test");
        String result = executor.execute();
        assertEquals("test", result);
    }

    @Test
    public void testExecute() {
        JavaScriptExecutor executor = JavaScriptExecutor.newExecutor("result.value = 'test'");
        String result = executor.execute();
        assertEquals("test", result);
    }

    @Test
    public void testExecuteWithBinding() {
        JavaScriptExecutor executor = JavaScriptExecutor.newExecutor("result.value = x + y");
        executor.bind("x", 5).bind("y", 3);
        Integer result = executor.execute();
        assertEquals(Integer.valueOf(8), result);
    }

    @Test(expected = RuntimeException.class)
    public void testExecuteWithInvalidScript() {
        JavaScriptExecutor executor = JavaScriptExecutor.newExecutor("invalid script");
        executor.execute();
    }

    @Test
    public void testExecuteWithComplexObject() {
        JavaScriptExecutor executor = JavaScriptExecutor.newExecutor(
            "var obj = {name: 'John', age: 30}; result.value = obj;");
        Map<String, Object> result = executor.execute();
        assertEquals("John", result.get("name"));
        assertEquals(30.0, result.get("age"));
    }

    @Test
    public void testExecuteWithMultipleBindings() {
        JavaScriptExecutor executor = JavaScriptExecutor.newExecutor(
            "result.value = {sum: a + b, product: a * b}");
        executor.bind("a", 5).bind("b", 3);
        Map<String, Object> result = executor.execute();
        assertEquals(8.0, result.get("sum"));
        assertEquals(15.0, result.get("product"));
    }

    @Test
    public void testExecuteWithNullBinding() {
        JavaScriptExecutor executor = JavaScriptExecutor.newExecutor(
            "result.value = (x === null) ? 'null' : 'not null'");
        executor.bind("x", null);
        String result = executor.execute();
        assertEquals("null", result);
    }

    @Test
    public void testExecuteWithUndefinedVariable() {
        JavaScriptExecutor executor = JavaScriptExecutor.newExecutor(
            "result.value = (typeof x === 'undefined') ? 'undefined' : 'defined'");
        String result = executor.execute();
        assertEquals("undefined", result);
    }
}
