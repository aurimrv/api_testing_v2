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
import org.zalando.catwatch.backend.repo.builder.BuilderUtil;
import java.util.HashSet;
import java.util.Set;
import java.lang.reflect.Field;

public class sonnet35_run01_BuilderUtilTest {
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
    public void testRandomProjectName() {
        Set<String> projectNames = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            String projectName = BuilderUtil.randomProjectName();
            assertNotNull(projectName);
            assertFalse(projectName.isEmpty());
            projectNames.add(projectName);
        }
        assertTrue(projectNames.size() > 1); // Ensure we're getting different names
    }

    @Test
    public void testRandomProjectNameInitialization() {
        // Test that the projectNames list is initialized on first call
        String firstProjectName = BuilderUtil.randomProjectName();
        assertNotNull(firstProjectName);
        assertFalse(firstProjectName.isEmpty());
    }

    @Test
    public void testRandomProjectNameCycling() {
        // Test that project names cycle after exhausting the list
        Set<String> allProjectNames = new HashSet<>();
        int cycleCount = 0;
        String firstProjectName = BuilderUtil.randomProjectName();
        allProjectNames.add(firstProjectName);

        while (cycleCount < 2) {
            String projectName = BuilderUtil.randomProjectName();
            if (projectName.equals(firstProjectName)) {
                cycleCount++;
            }
            allProjectNames.add(projectName);
        }

        assertTrue(allProjectNames.size() > 1);
    }

    @Test
    public void testRandomProjectNameThreadSafety() throws InterruptedException {
        // Test thread safety of randomProjectName method
        Set<String> projectNamesSet = new HashSet<>();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                projectNamesSet.add(BuilderUtil.randomProjectName());
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                projectNamesSet.add(BuilderUtil.randomProjectName());
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        assertTrue(projectNamesSet.size() > 1);
    }

    @Test
    public void testRandomProjectNameExceptionHandling() {
        // Test exception handling by simulating an IO error
        try {
            Field field = BuilderUtil.class.getDeclaredField("projectNames");
            field.setAccessible(true);
            field.set(null, null);

            // This should throw a RuntimeException
            BuilderUtil.randomProjectName();
            fail("Expected RuntimeException was not thrown");
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("never to happen"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Test setup failed: " + e.getMessage());
        }
    }
}