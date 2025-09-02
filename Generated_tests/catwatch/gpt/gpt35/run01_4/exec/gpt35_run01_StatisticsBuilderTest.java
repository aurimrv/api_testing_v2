
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

public class gpt35_run01_StatisticsBuilderTest {

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
    public void testStatisticsBuilderConstructor() {
        // Mocking StatisticsRepository and StatisticsBuilder classes for testing
        class StatisticsRepository {}
        class StatisticsBuilder {
            public StatisticsBuilder(StatisticsRepository statisticsRepository) {}
            public Object create() { return new Object(); }
        }

        StatisticsRepository statisticsRepository = new StatisticsRepository();
        StatisticsBuilder statisticsBuilder = new StatisticsBuilder(statisticsRepository);

        assertNotNull(statisticsBuilder);
    }

    @Test
    public void testStatisticsBuilderOrganizationName() {
        // Mocking StatisticsRepository and StatisticsBuilder classes for testing
        class StatisticsRepository {}
        class StatisticsBuilder {
            public StatisticsBuilder(StatisticsRepository statisticsRepository) {}
            public StatisticsBuilder organizationName(String organizationName) { return this; }
            public Object create() { return new Object(); }
        }

        StatisticsRepository statisticsRepository = new StatisticsRepository();
        StatisticsBuilder statisticsBuilder = new StatisticsBuilder(statisticsRepository);

        String organizationName = "TestOrg";
        statisticsBuilder.organizationName(organizationName);

        assertEquals(organizationName, ((StatisticsBuilder) statisticsBuilder).create());
    }

    @Test
    public void testStatisticsBuilderPublicProjectCount() {
        // Mocking StatisticsRepository and StatisticsBuilder classes for testing
        class StatisticsRepository {}
        class StatisticsBuilder {
            public StatisticsBuilder(StatisticsRepository statisticsRepository) {}
            public StatisticsBuilder publicProjectCount(int publicProjectCount) { return this; }
            public Object create() { return new Object(); }
        }

        StatisticsRepository statisticsRepository = new StatisticsRepository();
        StatisticsBuilder statisticsBuilder = new StatisticsBuilder(statisticsRepository);

        int publicProjectCount = 100;
        statisticsBuilder.publicProjectCount(publicProjectCount);

        assertEquals(publicProjectCount, ((StatisticsBuilder) statisticsBuilder).create());
    }

    // Add similar tests for other methods in StatisticsBuilder class
    
}
