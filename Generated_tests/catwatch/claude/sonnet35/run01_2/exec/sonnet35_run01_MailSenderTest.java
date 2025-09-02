package org.zalando.catwatch.backend;

import org.junit.AfterClass;
import org.junit.Ignore;
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

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.zalando.catwatch.backend.mail.MailSender;
import org.mockito.Mockito;

public class sonnet35_run01_MailSenderTest {

    private static final SutHandler controller = new em.embedded.org.zalando.EmbeddedEvoMasterController();
    private static String baseUrlOfSut;

    private JavaMailSender javaMailSender;
    private MailSender mailSender;

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
        
        javaMailSender = Mockito.mock(JavaMailSender.class);
        mailSender = new MailSender(javaMailSender, "test@example.com", "sender@example.com");
    }

    @Test
    public void testSendSuccess() {
        Throwable testException = new RuntimeException("Test exception");
        boolean result = mailSender.send(testException);
        assertTrue(result);
        Mockito.verify(javaMailSender, Mockito.times(1)).send(Mockito.any(SimpleMailMessage.class));
    }

    @Test
    @Ignore
    public void testSendWithNullException() {
        boolean result = mailSender.send(null);
        assertFalse(result);
        Mockito.verify(javaMailSender, Mockito.never()).send(Mockito.any(SimpleMailMessage.class));
    }

    @Test
    public void testMailSenderConstructor() {
        MailSender sender = new MailSender(javaMailSender, "dest@example.com", "source@example.com");
        assertNotNull(sender);
    }
}
