
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
import org.zalando.catwatch.backend.github.SnapshotProvider;
import org.zalando.catwatch.backend.model.util.Scorer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.Optional;

public class gpt4o_run01_SnapshotProviderTest {

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
    public void testSnapshotProviderConstructor() {
        Scorer scorer = new Scorer();
        String cachePath = "test-cache-path";
        Integer cacheSize = 10;
        String login = "test-login";
        String password = "test-password";
        String token = "test-token";

        SnapshotProvider snapshotProvider = new SnapshotProvider(scorer, cachePath, cacheSize, login, password, token);
        assertNotNull(snapshotProvider);
    }

    @Test
    public void testTakeSnapshotWithToken() throws Exception {
        Scorer scorer = new Scorer();
        SnapshotProvider snapshotProvider = new SnapshotProvider(scorer, "test-cache-path", 10, null, null, "test-token");

        // Ensure no exception is thrown while calling takeSnapshot
        try {
            snapshotProvider.takeSnapshot("test-org", new Date());
        } catch (Exception e) {
            fail("Expected no exception but got: " + e.getMessage());
        }
    }

    @Test
    public void testTakeSnapshotWithPassword() throws Exception {
        Scorer scorer = new Scorer();
        SnapshotProvider snapshotProvider = new SnapshotProvider(scorer, "test-cache-path", 10, "test-login", "test-password", null);

        // Ensure no exception is thrown while calling takeSnapshot
        try {
            snapshotProvider.takeSnapshot("test-org", new Date());
        } catch (Exception e) {
            fail("Expected no exception but got: " + e.getMessage());
        }
    }

    @Test
    public void testTakeSnapshotWithoutCredentials() {
        Scorer scorer = new Scorer();
        SnapshotProvider snapshotProvider = new SnapshotProvider(scorer, "test-cache-path", 10, null, null, null);

        // Ensure no exception is thrown while calling takeSnapshot
        try {
            snapshotProvider.takeSnapshot("test-org", new Date());
        } catch (IOException e) {
            fail("Expected no exception but got: " + e.getMessage());
        }
    }

    @Test
    public void testGetCacheDirectoryWritable() throws Exception {
        File tempDir = Files.createTempDirectory("test-cache").toFile();
        tempDir.setWritable(true);

        Scorer scorer = new Scorer();
        SnapshotProvider snapshotProvider = new SnapshotProvider(scorer, tempDir.getAbsolutePath(), 10, null, null, null);

        // Use reflection to access private method
        Optional<File> cacheDirectory = (Optional<File>) SnapshotProvider.class
            .getDeclaredMethod("getCacheDirectory")
            .invoke(snapshotProvider);

        assertTrue("Cache directory should be present", cacheDirectory.isPresent());
        assertEquals(tempDir.getAbsolutePath(), cacheDirectory.get().getAbsolutePath());
    }

    @Test
    public void testGetCacheDirectoryNotWritable() throws Exception {
        File tempDir = Files.createTempDirectory("test-cache").toFile();
        tempDir.setWritable(false);

        Scorer scorer = new Scorer();
        SnapshotProvider snapshotProvider = new SnapshotProvider(scorer, tempDir.getAbsolutePath(), 10, null, null, null);

        // Use reflection to access private method
        Optional<File> cacheDirectory = (Optional<File>) SnapshotProvider.class
            .getDeclaredMethod("getCacheDirectory")
            .invoke(snapshotProvider);

        assertFalse("Cache directory should not be present", cacheDirectory.isPresent());
    }

    @Test
    public void testGetCacheDirectoryCreationFailure() throws Exception {
        File tempDir = Files.createTempDirectory("test-cache").toFile();
        tempDir.delete(); // Ensure the directory is deleted to simulate creation failure

        Scorer scorer = new Scorer();
        SnapshotProvider snapshotProvider = new SnapshotProvider(scorer, tempDir.getAbsolutePath(), 10, null, null, null);

        // Use reflection to access private method
        Optional<File> cacheDirectory = (Optional<File>) SnapshotProvider.class
            .getDeclaredMethod("getCacheDirectory")
            .invoke(snapshotProvider);

        assertFalse("Cache directory should not be present on failure", cacheDirectory.isPresent());
    }

    @Test
    public void testInitWithoutCacheDirectory() {
        Scorer scorer = new Scorer();
        SnapshotProvider snapshotProvider = new SnapshotProvider(scorer, "non-existent-path", 10, null, null, null);

        try {
            snapshotProvider.init();

            // Use reflection to access private field
            Object httpClient = SnapshotProvider.class
                .getDeclaredMethod("getHttpClient")
                .invoke(snapshotProvider);

            assertNotNull("HttpClient should be initialized", httpClient);
        } catch (Exception e) {
            fail("Expected no exception but got: " + e.getMessage());
        }
    }
}
