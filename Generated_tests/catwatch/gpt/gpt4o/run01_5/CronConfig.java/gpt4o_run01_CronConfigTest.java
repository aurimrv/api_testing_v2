
package org.zalando.catwatch.backend;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.Map;
import java.util.List;
import java.util.Set;
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
import org.zalando.catwatch.backend.scheduler.CronConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

public class gpt4o_run01_CronConfigTest {

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
    public void testCronConfigConstructor() {
        CronConfig cronConfig = new CronConfig();
        assertNotNull(cronConfig);
    }

    @Test
    public void testCustomExitCodeGenerator() {
        CronConfig.CustomExitCodeGenerator exitCodeGenerator = new CronConfig.CustomExitCodeGenerator(0);
        assertNotNull(exitCodeGenerator);
        assertEquals(0, exitCodeGenerator.getExitCode());

        exitCodeGenerator = new CronConfig.CustomExitCodeGenerator(1);
        assertEquals(1, exitCodeGenerator.getExitCode());
    }

    @Test
    public void testCronBeanSuccess() throws Exception {
        CronConfig cronConfig = new CronConfig();
        MockRetryableFetcher fetcher = new MockRetryableFetcher(true);
        MockApplicationContext mockContext = new MockApplicationContext();

        ApplicationRunner runner = cronConfig.cron(fetcher, mockContext);
        runner.run(new MockApplicationArguments());

        assertTrue(fetcher.isDataFetched());
        assertEquals(0, mockContext.getExitCode());
    }

    @Test
    public void testCronBeanException() throws Exception {
        CronConfig cronConfig = new CronConfig();
        MockRetryableFetcher fetcher = new MockRetryableFetcher(false);
        MockApplicationContext mockContext = new MockApplicationContext();

        ApplicationRunner runner = cronConfig.cron(fetcher, mockContext);
        runner.run(new MockApplicationArguments());

        assertFalse(fetcher.isDataFetched());
        assertEquals(1, mockContext.getExitCode());
    }

    private static class MockRetryableFetcher implements CronConfig.RetryableFetcher {
        private final boolean fetchSuccess;
        private boolean dataFetched = false;

        MockRetryableFetcher(boolean fetchSuccess) {
            this.fetchSuccess = fetchSuccess;
        }

        @Override
        public void tryFetchData() throws Exception {
            if (!fetchSuccess) {
                throw new Exception("Fetch failed");
            }
            dataFetched = true;
        }

        public boolean isDataFetched() {
            return dataFetched;
        }
    }

    private static class MockApplicationArguments implements ApplicationArguments {

        @Override
        public String[] getSourceArgs() {
            return new String[0];
        }

        @Override
        public Set<String> getOptionNames() {
            return null;
        }

        @Override
        public boolean containsOption(String name) {
            return false;
        }

        @Override
        public List<String> getOptionValues(String name) {
            return null;
        }

        @Override
        public List<String> getNonOptionArgs() {
            return null;
        }
    }

    private static class MockApplicationContext implements ApplicationContext {
        private int exitCode;

        @Override
        public String getId() {
            return null;
        }

        @Override
        public String getApplicationName() {
            return null;
        }

        @Override
        public String getDisplayName() {
            return null;
        }

        @Override
        public long getStartupDate() {
            return 0;
        }

        @Override
        public ApplicationContext getParent() {
            return null;
        }

        @Override
        public void publishEvent(Object event) {
        }

        @Override
        public Object getBean(String name) {
            return null;
        }

        @Override
        public <T> T getBean(String name, Class<T> requiredType) {
            return null;
        }

        @Override
        public <T> T getBean(Class<T> requiredType) {
            return null;
        }

        @Override
        public Object getBean(String name, Object... args) {
            return null;
        }

        @Override
        public boolean containsBean(String name) {
            return false;
        }

        @Override
        public boolean isSingleton(String name) {
            return false;
        }

        @Override
        public boolean isPrototype(String name) {
            return false;
        }

        @Override
        public boolean isTypeMatch(String name, Class<?> targetType) {
            return false;
        }

        @Override
        public Class<?> getType(String name) {
            return null;
        }

        @Override
        public String[] getAliases(String name) {
            return new String[0];
        }

        @Override
        public Object getAutowireCapableBeanFactory() {
            return null;
        }

        public void setExitCode(int exitCode) {
            this.exitCode = exitCode;
        }

        public int getExitCode() {
            return exitCode;
        }
    }
}
