package eu.fayder.restcountries;

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

import eu.fayder.restcountries.domain.ResponseEntity;

public class sonnet35_run01_ResponseEntityTest {

    private static final SutHandler controller = new em.embedded.eu.fayder.EmbeddedEvoMasterController();
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

    @Test
    public void testConstructorAndGetters() {
        int status = 200;
        String message = "OK";
        ResponseEntity responseEntity = new ResponseEntity(status, message);

        assertEquals(status, responseEntity.getStatus());
        assertEquals(message, responseEntity.getMessage());
    }

    @Test
    public void testGetMessage() {
        ResponseEntity responseEntity = new ResponseEntity(404, "Not Found");
        assertEquals("Not Found", responseEntity.getMessage());
    }

    @Test
    public void testGetStatus() {
        ResponseEntity responseEntity = new ResponseEntity(500, "Internal Server Error");
        assertEquals(500, responseEntity.getStatus());
    }

    @Test
    public void testNullMessage() {
        ResponseEntity responseEntity = new ResponseEntity(200, null);
        assertNull(responseEntity.getMessage());
    }

    @Test
    public void testNegativeStatus() {
        ResponseEntity responseEntity = new ResponseEntity(-1, "Negative Status");
        assertEquals(-1, responseEntity.getStatus());
    }

    @Test
    public void testZeroStatus() {
        ResponseEntity responseEntity = new ResponseEntity(0, "Zero Status");
        assertEquals(0, responseEntity.getStatus());
    }

    @Test
    public void testEmptyMessage() {
        ResponseEntity responseEntity = new ResponseEntity(204, "");
        assertEquals("", responseEntity.getMessage());
    }

    @Test
    public void testLargeStatus() {
        ResponseEntity responseEntity = new ResponseEntity(Integer.MAX_VALUE, "Max Status");
        assertEquals(Integer.MAX_VALUE, responseEntity.getStatus());
    }

    @Test
    public void testLongMessage() {
        String longMessage = new String(new char[1000]).replace('\0', 'a');
        ResponseEntity responseEntity = new ResponseEntity(200, longMessage);
        assertEquals(longMessage, responseEntity.getMessage());
    }
}
