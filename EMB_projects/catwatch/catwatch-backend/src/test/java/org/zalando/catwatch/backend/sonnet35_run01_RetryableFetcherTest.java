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
import org.zalando.catwatch.backend.scheduler.RetryableFetcher;
import org.zalando.catwatch.backend.scheduler.Fetcher;
import org.zalando.catwatch.backend.mail.MailSender;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.mockito.Mockito;

public class sonnet35_run01_RetryableFetcherTest {

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
    public void testTryFetchData() throws Exception {
        Fetcher mockFetcher = Mockito.mock(Fetcher.class);
        MailSender mockMailSender = Mockito.mock(MailSender.class);
        
        RetryableFetcher retryableFetcher = new RetryableFetcher(mockFetcher, 3, 1000, 5000, 2.0, mockMailSender);
        
        Mockito.when(mockFetcher.fetchData()).thenReturn(true);
        
        retryableFetcher.tryFetchData();
        
        Mockito.verify(mockFetcher, Mockito.times(1)).fetchData();
        Mockito.verify(mockMailSender, Mockito.never()).send(Mockito.any());
    }

    @Test
    public void testTryFetchDataWithRetry() throws Exception {
        Fetcher mockFetcher = Mockito.mock(Fetcher.class);
        MailSender mockMailSender = Mockito.mock(MailSender.class);
        
        RetryableFetcher retryableFetcher = new RetryableFetcher(mockFetcher, 3, 1000, 5000, 2.0, mockMailSender);
        
        Mockito.when(mockFetcher.fetchData())
               .thenThrow(new RuntimeException("Test exception"))
               .thenThrow(new RuntimeException("Test exception"))
               .thenReturn(true);
        
        retryableFetcher.tryFetchData();
        
        Mockito.verify(mockFetcher, Mockito.times(3)).fetchData();
        Mockito.verify(mockMailSender, Mockito.never()).send(Mockito.any());
    }

    @Test
    public void testTryFetchDataWithMaxRetries() throws Exception {
        Fetcher mockFetcher = Mockito.mock(Fetcher.class);
        MailSender mockMailSender = Mockito.mock(MailSender.class);
        
        RetryableFetcher retryableFetcher = new RetryableFetcher(mockFetcher, 3, 1000, 5000, 2.0, mockMailSender);
        
        Mockito.when(mockFetcher.fetchData()).thenThrow(new RuntimeException("Test exception"));
        
        retryableFetcher.tryFetchData();
        
        Mockito.verify(mockFetcher, Mockito.times(3)).fetchData();
        Mockito.verify(mockMailSender, Mockito.times(1)).send(Mockito.any());
    }

    @Test
    public void testConstructor() {
        Fetcher mockFetcher = Mockito.mock(Fetcher.class);
        MailSender mockMailSender = Mockito.mock(MailSender.class);
        
        RetryableFetcher retryableFetcher = new RetryableFetcher(mockFetcher, 3, 1000, 5000, 2.0, mockMailSender);
        
        assertNotNull(retryableFetcher);
    }
}
