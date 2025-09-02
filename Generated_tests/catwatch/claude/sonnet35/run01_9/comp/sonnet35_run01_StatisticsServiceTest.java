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

import org.zalando.catwatch.backend.service.StatisticsService;
import org.zalando.catwatch.backend.model.Statistics;
import org.zalando.catwatch.backend.repo.StatisticsRepository;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

public class sonnet35_run01_StatisticsServiceTest {

    private static final SutHandler controller = new em.embedded.org.zalando.EmbeddedEvoMasterController();
    private static String baseUrlOfSut;

    private StatisticsService statisticsService;
    private StatisticsRepository mockRepository;

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
        mockRepository = Mockito.mock(StatisticsRepository.class);
        statisticsService = new StatisticsService();
    }

    @Test
    public void testGetStatisticsWithoutDates() {
        Collection<String> organizations = Arrays.asList("org1", "org2");
        List<Statistics> mockStats = new ArrayList<>();
        mockStats.add(new Statistics(1L, new Date()));
        Mockito.when(mockRepository.findByOrganizationNameOrderByKeySnapshotDateDesc(Mockito.anyString(), Mockito.any(PageRequest.class)))
               .thenReturn(mockStats);

        Collection<Statistics> result = StatisticsService.getStatistics(mockRepository, organizations, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetStatisticsWithDates() {
        Collection<String> organizations = Arrays.asList("org1", "org2");
        String startDate = "2023-01-01";
        String endDate = "2023-12-31";
        List<Statistics> mockStats = new ArrayList<>();
        mockStats.add(new Statistics(1L, new Date()));
        Mockito.when(mockRepository.findStatisticsByOrganizationAndDate(Mockito.anyString(), Mockito.any(Date.class), Mockito.any(Date.class)))
               .thenReturn(mockStats);

        Collection<Statistics> result = StatisticsService.getStatistics(mockRepository, organizations, startDate, endDate);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatisticsWithInvalidStartDate() {
        Collection<String> organizations = Arrays.asList("org1");
        StatisticsService.getStatistics(mockRepository, organizations, "invalid-date", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStatisticsWithInvalidEndDate() {
        Collection<String> organizations = Arrays.asList("org1");
        StatisticsService.getStatistics(mockRepository, organizations, null, "invalid-date");
    }

    @Test
    public void testAggregateStatistics() {
        List<Statistics> statsList = new ArrayList<>();
        statsList.add(createMockStatistics(1, 10, 5, 20, 30));
        statsList.add(createMockStatistics(2, 15, 8, 25, 35));

        Statistics result = StatisticsService.aggregateStatistics(statsList);

        assertNotNull(result);
        assertEquals(25, result.getAllContributorsCount().intValue());
        assertEquals(13, result.getExternalContributorsCount().intValue());
        assertEquals(45, result.getAllForksCount().intValue());
        assertEquals(65, result.getAllStarsCount().intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAggregateStatisticsWithEmptyList() {
        StatisticsService.aggregateStatistics(new ArrayList<>());
    }

    @Test
    public void testAggregateHistoricalStatistics() {
        List<List<Statistics>> statisticsLists = new ArrayList<>();
        List<Statistics> list1 = new ArrayList<>();
        list1.add(createMockStatistics(1, 10, 5, 20, 30));
        List<Statistics> list2 = new ArrayList<>();
        list2.add(createMockStatistics(2, 15, 8, 25, 35));
        statisticsLists.add(list1);
        statisticsLists.add(list2);

        Collection<Statistics> result = StatisticsService.aggregateHistoricalStatistics(statisticsLists);

        assertNotNull(result);
        assertEquals(1, result.size());
        Statistics aggregated = result.iterator().next();
        assertEquals(25, aggregated.getAllContributorsCount().intValue());
        assertEquals(13, aggregated.getExternalContributorsCount().intValue());
        assertEquals(45, aggregated.getAllForksCount().intValue());
        assertEquals(65, aggregated.getAllStarsCount().intValue());
    }

    @Test
    public void testAggregateHistoricalStatisticsWithEmptyList() {
        Collection<Statistics> result = StatisticsService.aggregateHistoricalStatistics(new ArrayList<>());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAggregateHistoricalStatisticsWithSingleList() {
        List<List<Statistics>> statisticsLists = new ArrayList<>();
        List<Statistics> list = new ArrayList<>();
        list.add(createMockStatistics(1, 10, 5, 20, 30));
        statisticsLists.add(list);

        Collection<Statistics> result = StatisticsService.aggregateHistoricalStatistics(statisticsLists);

        assertNotNull(result);
        assertEquals(1, result.size());
        Statistics aggregated = result.iterator().next();
        assertEquals(10, aggregated.getAllContributorsCount().intValue());
        assertEquals(5, aggregated.getExternalContributorsCount().intValue());
        assertEquals(20, aggregated.getAllForksCount().intValue());
        assertEquals(30, aggregated.getAllStarsCount().intValue());
    }

    private Statistics createMockStatistics(long id, int contributors, int externalContributors, int forks, int stars) {
        Statistics stats = new Statistics(id, new Date());
        stats.setAllContributorsCount(contributors);
        stats.setExternalContributorsCount(externalContributors);
        stats.setAllForksCount(forks);
        stats.setAllStarsCount(stars);
        return stats;
    }
}
