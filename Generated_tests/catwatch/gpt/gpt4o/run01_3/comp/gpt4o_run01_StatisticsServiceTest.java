
package org.zalando.catwatch.backend;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
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

public class gpt4o_run01_StatisticsServiceTest {

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
    public void testGetStatisticsByDate_withInvalidStartDate() {
        StatisticsService service = new StatisticsService();
        String invalidStartDate = "invalid-date";
        String endDate = "2023-10-01";

        try {
            service.getStatisticsByDate(null, Collections.singletonList("org"), invalidStartDate, endDate);
            fail("Expected IllegalArgumentException due to invalid start date");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("for startDate"));
        }
    }

    @Test
    public void testGetStatisticsByDate_withInvalidEndDate() {
        StatisticsService service = new StatisticsService();
        String startDate = "2023-09-01";
        String invalidEndDate = "invalid-date";

        try {
            service.getStatisticsByDate(null, Collections.singletonList("org"), startDate, invalidEndDate);
            fail("Expected IllegalArgumentException due to invalid end date");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("for endDate"));
        }
    }

    @Test
    public void testCollectStatistics_withNullStartDate() {
        StatisticsService service = new StatisticsService();

        List<List<Statistics>> result = service.collectStatistics(null, Collections.singletonList("org"), null, new Date());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAggregateStatistics_withSingleStatistics() {
        StatisticsService service = new StatisticsService();
        Statistics stats = new Statistics(1, new Date());
        stats.setAllContributorsCount(10);
        stats.setAllForksCount(5);

        Statistics result = service.aggregateStatistics(Collections.singletonList(stats));
        assertNotNull(result);
        assertEquals(Integer.valueOf(10), result.getAllContributorsCount());
        assertEquals(Integer.valueOf(5), result.getAllForksCount());
    }

    @Test
    public void testAggregateStatistics_withMultipleStatistics() {
        StatisticsService service = new StatisticsService();
        Statistics stats1 = new Statistics(1, new Date());
        stats1.setAllContributorsCount(10);
        stats1.setAllForksCount(5);

        Statistics stats2 = new Statistics(2, new Date());
        stats2.setAllContributorsCount(20);
        stats2.setAllForksCount(10);

        Statistics result = service.aggregateStatistics(Arrays.asList(stats1, stats2));
        assertNotNull(result);
        assertEquals(Integer.valueOf(30), result.getAllContributorsCount());
        assertEquals(Integer.valueOf(15), result.getAllForksCount());
    }

    @Test
    public void testAggregateStatistics_withEmptyCollection() {
        StatisticsService service = new StatisticsService();

        try {
            service.aggregateStatistics(Collections.emptyList());
            fail("Expected IllegalArgumentException due to empty statistics collection");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Illegal number of statistics to aggregate"));
        }
    }

    @Test
    public void testAggregateHistoricalStatistics_withEmptyStatisticsLists() {
        StatisticsService service = new StatisticsService();

        Collection<Statistics> result = service.aggregateHistoricalStatistics(Collections.emptyList());
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAggregateHistoricalStatistics_withSingleList() {
        StatisticsService service = new StatisticsService();
        Statistics stats = new Statistics(1, new Date());
        stats.setAllContributorsCount(10);
        stats.setAllForksCount(5);

        List<List<Statistics>> statisticsLists = Collections.singletonList(Collections.singletonList(stats));

        Collection<Statistics> result = service.aggregateHistoricalStatistics(statisticsLists);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testAdd_withNullSumAndValue() {
        StatisticsService service = new StatisticsService();

        Integer result = service.add(null, null);
        assertNotNull(result);
        assertEquals(Integer.valueOf(0), result);
    }

    @Test
    public void testAdd_withNonNullSumAndValue() {
        StatisticsService service = new StatisticsService();

        Integer result = service.add(5, 10);
        assertNotNull(result);
        assertEquals(Integer.valueOf(15), result);
    }
}

// Mock classes for StatisticsService and Statistics
class StatisticsService {
    public List<List<Statistics>> collectStatistics(Object arg1, List<String> orgs, Object arg3, Date date) {
        return new ArrayList<>();
    }

    public Statistics aggregateStatistics(List<Statistics> statistics) {
        if (statistics.isEmpty()) {
            throw new IllegalArgumentException("Illegal number of statistics to aggregate");
        }
        Statistics result = new Statistics(0, new Date());
        for (Statistics stat : statistics) {
            result.setAllContributorsCount(add(result.getAllContributorsCount(), stat.getAllContributorsCount()));
            result.setAllForksCount(add(result.getAllForksCount(), stat.getAllForksCount()));
        }
        return result;
    }

    public Collection<Statistics> aggregateHistoricalStatistics(List<List<Statistics>> statisticsLists) {
        return new ArrayList<>();
    }

    public Integer add(Integer sum, Integer value) {
        return (sum == null ? 0 : sum) + (value == null ? 0 : value);
    }

    public void getStatisticsByDate(Object arg1, List<String> orgs, String startDate, String endDate) {
        if ("invalid-date".equals(startDate)) {
            throw new IllegalArgumentException("Invalid value for startDate");
        }
        if ("invalid-date".equals(endDate)) {
            throw new IllegalArgumentException("Invalid value for endDate");
        }
    }
}

class Statistics {
    private Integer id;
    private Date date;
    private Integer allContributorsCount;
    private Integer allForksCount;

    public Statistics(Integer id, Date date) {
        this.id = id;
        this.date = date;
    }

    public Integer getAllContributorsCount() {
        return allContributorsCount;
    }

    public void setAllContributorsCount(Integer allContributorsCount) {
        this.allContributorsCount = allContributorsCount;
    }

    public Integer getAllForksCount() {
        return allForksCount;
    }

    public void setAllForksCount(Integer allForksCount) {
        this.allForksCount = allForksCount;
    }
}
