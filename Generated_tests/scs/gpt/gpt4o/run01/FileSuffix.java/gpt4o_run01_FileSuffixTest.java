
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

public class gpt4o_run01_FileSuffixTest {

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
    public void testConstructorCoverage() {
        // Explicitly instantiate the class to cover <init> method
        new org.restscs.imp.FileSuffix();
    }

    @Test
    public void testFileSuffixForTextDirectoryWithTxtFile() {
        given()
            .pathParam("directory", "text")
            .pathParam("file", "example.txt")
        .when()
            .get(baseUrlOfSut + "/api/filesuffix/{directory}/{file}")
        .then()
            .statusCode(200)
            .body(equalTo("1"));
    }

    @Test
    public void testFileSuffixForAcrobatDirectoryWithPdfFile() {
        given()
            .pathParam("directory", "acrobat")
            .pathParam("file", "document.pdf")
        .when()
            .get(baseUrlOfSut + "/api/filesuffix/{directory}/{file}")
        .then()
            .statusCode(200)
            .body(equalTo("2"));
    }

    @Test
    public void testFileSuffixForWordDirectoryWithDocFile() {
        given()
            .pathParam("directory", "word")
            .pathParam("file", "file.doc")
        .when()
            .get(baseUrlOfSut + "/api/filesuffix/{directory}/{file}")
        .then()
            .statusCode(200)
            .body(equalTo("3"));
    }

    @Test
    public void testFileSuffixForBinDirectoryWithExeFile() {
        given()
            .pathParam("directory", "bin")
            .pathParam("file", "program.exe")
        .when()
            .get(baseUrlOfSut + "/api/filesuffix/{directory}/{file}")
        .then()
            .statusCode(404)
            .body(equalTo("4"));
    }

    @Test
    public void testFileSuffixForLibDirectoryWithDllFile() {
        given()
            .pathParam("directory", "lib")
            .pathParam("file", "library.dll")
        .when()
            .get(baseUrlOfSut + "/api/filesuffix/{directory}/{file}")
        .then()
            .statusCode(404)
            .body(equalTo("5"));
    }

    @Test
    public void testFileSuffixForInvalidDirectory() {
        given()
            .pathParam("directory", "invalid")
            .pathParam("file", "file.doc")
        .when()
            .get(baseUrlOfSut + "/api/filesuffix/{directory}/{file}")
        .then()
            .statusCode(404)
            .body(equalTo("0"));
    }

    @Test
    public void testFileSuffixForFileWithoutExtension() {
        given()
            .pathParam("directory", "text")
            .pathParam("file", "noextension")
        .when()
            .get(baseUrlOfSut + "/api/filesuffix/{directory}/{file}")
        .then()
            .statusCode(404)
            .body(equalTo("0"));
    }

    @Test
    public void testFileSuffixForEmptyFileName() {
        given()
            .pathParam("directory", "text")
            .pathParam("file", "")
        .when()
            .get(baseUrlOfSut + "/api/filesuffix/{directory}/{file}")
        .then()
            .statusCode(404)
            .body(equalTo("0"));
    }

    @Test
    public void testFileSuffixForEmptyDirectory() {
        given()
            .pathParam("directory", "")
            .pathParam("file", "file.txt")
        .when()
            .get(baseUrlOfSut + "/api/filesuffix/{directory}/{file}")
        .then()
            .statusCode(404)
            .body(equalTo("0"));
    }

    @Test
    public void testFileSuffixForEmptyDirectoryAndFile() {
        given()
            .pathParam("directory", "")
            .pathParam("file", "")
        .when()
            .get(baseUrlOfSut + "/api/filesuffix/{directory}/{file}")
        .then()
            .statusCode(404)
            .body(equalTo("0"));
    }

    @Test
    public void testFileSuffixForInvalidExtension() {
        given()
            .pathParam("directory", "text")
            .pathParam("file", "file.invalid")
        .when()
            .get(baseUrlOfSut + "/api/filesuffix/{directory}/{file}")
        .then()
            .statusCode(404)
            .body(equalTo("0"));
    }
}
