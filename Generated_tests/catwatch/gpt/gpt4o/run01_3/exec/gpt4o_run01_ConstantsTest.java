
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

public class gpt4o_run01_ConstantsTest {

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
    public void testConstantsInitialization() {
        // Explicitly instantiate the Constants class to test the <init> constructor
        org.zalando.catwatch.backend.util.Constants constants = new org.zalando.catwatch.backend.util.Constants();
        assertNotNull(constants);

        // Validate the defined constants
        assertEquals("/contributors", org.zalando.catwatch.backend.util.Constants.API_RESOURCE_CONTRIBUTORS);
        assertEquals("/statistics", org.zalando.catwatch.backend.util.Constants.API_RESOURCE_STATISTICS);
        assertEquals("/projects", org.zalando.catwatch.backend.util.Constants.API_RESOURCE_PROJECTS);
        assertEquals("/languages", org.zalando.catwatch.backend.util.Constants.API_RESOURCE_LANGUAGES);

        assertEquals("end_date", org.zalando.catwatch.backend.util.Constants.API_REQUEST_PARAM_ENDDATE);
        assertEquals("start_date", org.zalando.catwatch.backend.util.Constants.API_REQUEST_PARAM_STARTDATE);
        assertEquals("sortBy", org.zalando.catwatch.backend.util.Constants.API_REQUEST_PARAM_SORTBY);
        assertEquals("organizations", org.zalando.catwatch.backend.util.Constants.API_REQUEST_PARAM_ORGANIZATIONS);
        assertEquals("limit", org.zalando.catwatch.backend.util.Constants.API_REQUEST_PARAM_LIMIT);
        assertEquals("offset", org.zalando.catwatch.backend.util.Constants.API_REQUEST_PARAM_OFFSET);
        assertEquals("q", org.zalando.catwatch.backend.util.Constants.API_REQUEST_PARAM_Q);
        assertEquals("language", org.zalando.catwatch.backend.util.Constants.API_REQUEST_PARAM_LANGUAGE);

        assertEquals("organization.list", org.zalando.catwatch.backend.util.Constants.CONFIG_ORGANIZATION_LIST);
        assertEquals("default.item.limit", org.zalando.catwatch.backend.util.Constants.CONFIG_DEFAULT_LIMIT);

        assertEquals("Invalid date format", org.zalando.catwatch.backend.util.Constants.ERR_MSG_WRONG_DATE_FORMAT);
    }
}
