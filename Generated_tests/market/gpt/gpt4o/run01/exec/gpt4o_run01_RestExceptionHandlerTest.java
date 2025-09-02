
package market;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;
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
import static org.evomaster.client.java.sql.dsl.SqlDsl.sql;
import org.evomaster.client.java.controller.api.dto.database.operations.InsertionResultsDto;
import org.evomaster.client.java.controller.api.dto.database.operations.InsertionDto;
import static org.evomaster.client.java.controller.contentMatchers.NumberMatcher.*;
import static org.evomaster.client.java.controller.contentMatchers.StringMatcher.*;
import static org.evomaster.client.java.controller.contentMatchers.SubStringMatcher.*;
import static org.evomaster.client.java.controller.expect.ExpectationHandler.expectationHandler;
import org.evomaster.client.java.controller.expect.ExpectationHandler;
import io.restassured.path.json.JsonPath;
import java.util.Arrays;
import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;

import java.util.Locale;
import java.lang.reflect.Method;

public class gpt4o_run01_RestExceptionHandlerTest {

    private static final SutHandler controller = new em.embedded.market.EmbeddedEvoMasterController();
    private static String baseUrlOfSut;

    @BeforeAll
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

    @AfterAll
    public static void tearDown() {
        controller.stopSut();
    }

    @BeforeEach
    public void initTest() {
        controller.resetDatabase(Arrays.asList("USER_ROLE", "CUSTOMER_ORDER", "CART_ITEM", "PRODUCT", "CART", "CONTACTS"));
        controller.resetStateOfSUT();
    }

    private String invokeResolveErrorMessage(Object handler, FieldError fieldError) throws Exception {
        Method method = handler.getClass().getDeclaredMethod("resolveErrorMessage", FieldError.class);
        method.setAccessible(true);
        return (String) method.invoke(handler, fieldError);
    }

    @Test
    public void testResolveErrorMessageLocalizedMessageEqualsDefaultMessage() throws Exception {
        // Instantiating the RestExceptionHandler to execute constructor
        MessageSource mockMessageSource = org.mockito.Mockito.mock(MessageSource.class);
        market.rest.exception.RestExceptionHandler handler = new market.rest.exception.RestExceptionHandler(mockMessageSource);

        // Mocking a FieldError instance
        FieldError fieldError = new FieldError("objectName", "field", null, false, new String[]{"code1", "code2"}, null, "defaultMessage");
        org.mockito.Mockito.when(mockMessageSource.getMessage(org.mockito.Mockito.eq(fieldError), org.mockito.Mockito.any(Locale.class)))
                .thenReturn("defaultMessage");

        // Invoking resolveErrorMessage using reflection
        String result = invokeResolveErrorMessage(handler, fieldError);

        // Assert the result falls back to the first "code" when localized matches default
        assertEquals("code1", result);
    }

    @Test
    public void testResolveErrorMessageLocalizedMessageDifferentFromDefault() throws Exception {
        // Instantiating the RestExceptionHandler to execute constructor
        MessageSource mockMessageSource = org.mockito.Mockito.mock(MessageSource.class);
        market.rest.exception.RestExceptionHandler handler = new market.rest.exception.RestExceptionHandler(mockMessageSource);

        // Mocking a FieldError instance
        FieldError fieldError = new FieldError("objectName", "field", null, false, new String[]{"code1", "code2"}, null, "defaultMessage");
        org.mockito.Mockito.when(mockMessageSource.getMessage(org.mockito.Mockito.eq(fieldError), org.mockito.Mockito.any(Locale.class)))
                .thenReturn("localizedMessage");

        // Invoking resolveErrorMessage using reflection
        String result = invokeResolveErrorMessage(handler, fieldError);

        // Assert the localized message is returned
        assertEquals("localizedMessage", result);
    }

    @Test
    public void testResolveErrorMessageNullFieldErrorCodes() throws Exception {
        // Instantiating the RestExceptionHandler to execute constructor
        MessageSource mockMessageSource = org.mockito.Mockito.mock(MessageSource.class);
        market.rest.exception.RestExceptionHandler handler = new market.rest.exception.RestExceptionHandler(mockMessageSource);

        // Mocking a FieldError instance with null codes
        FieldError fieldError = new FieldError("objectName", "field", null, false, null, null, "defaultMessage");
        org.mockito.Mockito.when(mockMessageSource.getMessage(org.mockito.Mockito.eq(fieldError), org.mockito.Mockito.any(Locale.class)))
                .thenReturn("defaultMessage");

        // Invoking resolveErrorMessage using reflection
        String result = invokeResolveErrorMessage(handler, fieldError);

        // Assert the result matches the default message
        assertEquals("defaultMessage", result);
    }
}
