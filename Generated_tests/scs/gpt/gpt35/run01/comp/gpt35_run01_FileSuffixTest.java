
package org.restscs;

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

import org.restscs.imp.FileSuffix;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class gpt35_run01_FileSuffixTest {

    private static final SutHandler controller = new em.embedded.org.restscs.EmbeddedEvoMasterController();
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
    public void testFileSuffixConstructor() {
        FileSuffix fileSuffix = new FileSuffix();
        assertNotNull(fileSuffix);
    }

    @Test
    public void testFileSuffixSubject() {
        // Test directory "text" with file "example.txt"
        assertEquals("1", FileSuffix.subject("text", "example.txt"));

        // Test directory "acrobat" with file "document.pdf"
        assertEquals("2", FileSuffix.subject("acrobat", "document.pdf"));

        // Test directory "word" with file "report.doc"
        assertEquals("3", FileSuffix.subject("word", "report.doc"));

        // Test directory "bin" with file "program.exe"
        assertEquals("4", FileSuffix.subject("bin", "program.exe"));

        // Test directory "lib" with file "library.dll"
        assertEquals("5", FileSuffix.subject("lib", "library.dll"));

        // Test directory "unknown" with file "unknown.file"
        assertEquals("0", FileSuffix.subject("unknown", "unknown.file"));

        // Test empty directory with file "file.txt"
        assertEquals("0", FileSuffix.subject("", "file.txt"));
    }
}
