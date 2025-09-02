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
import org.zalando.catwatch.backend.util.StringParser;
import java.util.Collection;
import java.util.Date;
import java.text.ParseException;

public class sonnet35_run01_StringParserTest {

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
    public void testParseStringListWithNullInput() {
        Collection<String> result = StringParser.parseStringList(null, ",");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testParseStringListWithNoDelimiter() {
        Collection<String> result = StringParser.parseStringList("singleItem", ",");
        assertEquals(1, result.size());
        assertTrue(result.contains("singleItem"));
    }

    @Test
    public void testParseStringListWithDelimiter() {
        Collection<String> result = StringParser.parseStringList("foo, bar, lock", ",");
        assertEquals(3, result.size());
        assertTrue(result.contains("foo"));
        assertTrue(result.contains("bar"));
        assertTrue(result.contains("lock"));
    }

    @Test
    public void testGetISO8601StringForDate() {
        Date date = new Date(1609459200000L); // 2021-01-01 00:00:00 UTC
        String result = StringParser.getISO8601StringForDate(date);
        assertEquals("2021-01-01T00:00:00Z", result);
    }

    @Test
    public void testParseIso8601Date() throws ParseException {
        String dateString = "2021-01-01T00:00:00Z";
        Date result = StringParser.parseIso8601Date(dateString);
        assertEquals(1609459200000L, result.getTime());
    }

    @Test(expected = ParseException.class)
    public void testParseIso8601DateWithInvalidFormat() throws ParseException {
        StringParser.parseIso8601Date("invalid-date-format");
    }

    @Test
    public void testStringParserConstructor() {
        StringParser parser = new StringParser();
        assertNotNull(parser);
    }
}
