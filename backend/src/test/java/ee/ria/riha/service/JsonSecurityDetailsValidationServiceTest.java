package ee.ria.riha.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class JsonSecurityDetailsValidationServiceTest {

    private static final String DEFAULT_VALIDATION_ERROR_MESSAGE = "securityDetailsValidationError";
    //validation process consists of 3 parts where each part generates maximum 1 validation error message
    //even though there may be more of them
    private static final int MAX_AMOUNT_OF_VALIDATION_ERRORS = 3;

    private static final String SECURITY_CLASS_IS_NULL_KEY = "validation.system.json.securityClass.isNullWhenStandardIsISKE";
    private static final String SECURITY_CLASS_IS_NOT_NULL_KEY = "validation.system.json.securityClass.isNotNullWhenStandardIsNotISKE";
    private static final String SECURITY_LEVEL_IS_NULL_KEY = "validation.system.json.securityLevel.isNullWhenStandardIsISKE";
    private static final String SECURITY_LEVEL_IS_NOT_NULL_KEY = "validation.system.json.securityLevel.isNotNullWhenStandardIsNotISKE";
    private static final String LEVEL_DOES_NOT_MATCH_CLASS_KEY = "validation.system.json.securityLevel.doesNotMatchSecurityClass";
    private static final String LATEST_AUDIT_RESOLUTION_IS_NULL_KEY = "validation.system.json.latestAuditResolution.isNullWhenLatestAuditDateIsNotNull";
    private static final String LATEST_AUDIT_RESOLUTION_IS_NOT_NULL_KEY = "validation.system.json.latestAuditResolution.isNotNullWhenLatestAuditDateIsNull";
    private static final String LATEST_AUDIT_RESOLUTION_VALUE_IS_NOT_ALLOWED_KEY = "validation.system.json.latestAuditResolution.valueIsNotAllowed";

    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private JsonSecurityDetailsValidationService jsonSecurityDetailsValidationService;

    @Before
    public void setUp() {
        when(messageSource.getMessage(any(String.class), any(), any(Locale.class))).thenReturn(DEFAULT_VALIDATION_ERROR_MESSAGE);
    }

    @Test
    public void successfullyValidatesCorrectJson() {
        String json = "{\n" +
                "  \"security\": " +
                "  {\n" +
                "    \"standard\": \"ISKE\",\n" +
                "    \"class\": \"K1T2S1\",\n" +
                "    \"level\": \"M\",\n" +
                "    \"latest_audit_date\": \"2017-11-13T17:15:55.002+02:00\",\n" +
                "    \"latest_audit_resolution\": \"Auditeeritud märkuste või soovitustega\"\n" +
                "  }\n" +
                "}";
        List<ProcessingMessage> validationErrorMessages = jsonSecurityDetailsValidationService.validate(fromString(json));
        assertThat(validationErrorMessages, is(emptyCollectionOf(ProcessingMessage.class)));
    }

    @Test
    public void catchesMaxAmountOfValidationErrorMessages() {
        String json = "{\n" +
                "  \"security\": " +
                "  {\n" +
                "    \"standard\": \"ISKE\",\n" +
                "    \"class\": null,\n" +
                "    \"level\": null,\n" +
                "    \"latest_audit_date\": null,\n" +
                "    \"latest_audit_resolution\": \"Auditeeritud märkuste või soovitustega\"\n" +
                "  }\n" +
                "}";
        List<ProcessingMessage> validationErrorMessages = jsonSecurityDetailsValidationService.validate(fromString(json));
        assertThat(validationErrorMessages.size(), is(equalTo(MAX_AMOUNT_OF_VALIDATION_ERRORS)));
        verify(messageSource).getMessage(SECURITY_CLASS_IS_NULL_KEY, null, Locale.getDefault());
        verify(messageSource).getMessage(SECURITY_LEVEL_IS_NULL_KEY, null, Locale.getDefault());
        verify(messageSource).getMessage(LATEST_AUDIT_RESOLUTION_IS_NOT_NULL_KEY, null, Locale.getDefault());
    }

    @Test
    public void securityClassMustBeNullWhenStandardIsNotISKE() {
        String json = "{\n" +
                "  \"security\": " +
                "  {\n" +
                "    \"standard\": \"This is not ISKE\",\n" +
                "    \"class\": \"K1T2S1\"\n" +
                "  }\n" +
                "}";
        ProcessingMessage securityClassValidationErrorMessage = jsonSecurityDetailsValidationService.validateSecurityClass(fromString(json));
        assertThat(securityClassValidationErrorMessage, is(notNullValue()));
        verify(messageSource).getMessage(SECURITY_CLASS_IS_NOT_NULL_KEY, null, Locale.getDefault());
    }

    @Test
    public void securityClassMustNotBeNullWhenStandardIsISKE() {
        String json = "{\n" +
                "  \"security\": " +
                "  {\n" +
                "    \"standard\": \"ISKE\",\n" +
                "    \"class\": null\n" +
                "  }\n" +
                "}";
        ProcessingMessage securityClassValidationErrorMessage = jsonSecurityDetailsValidationService.validateSecurityClass(fromString(json));
        assertThat(securityClassValidationErrorMessage, is(notNullValue()));
        verify(messageSource).getMessage(SECURITY_CLASS_IS_NULL_KEY, null, Locale.getDefault());
    }

    @Test
    public void securityLevelMustBeNullWhenStandardIsNotISKE() {
        String json = "{\n" +
                "  \"security\": " +
                "  {\n" +
                "    \"standard\": \"This is not ISKE\",\n" +
                "    \"class\": \"K1T2S1\",\n" +
                "    \"level\": \"M\"\n" +
                "  }\n" +
                "}";
        ProcessingMessage securityLevelValidationErrorMessage = jsonSecurityDetailsValidationService.validateSecurityLevel(fromString(json));
        assertThat(securityLevelValidationErrorMessage, is(notNullValue()));
        verify(messageSource).getMessage(SECURITY_LEVEL_IS_NOT_NULL_KEY, null, Locale.getDefault());
    }

    @Test
    public void securityLevelMustNotBeNullWhenStandardIsISKE() {
        String json = "{\n" +
                "  \"security\": " +
                "  {\n" +
                "    \"standard\": \"ISKE\",\n" +
                "    \"class\": \"K1T2S1\",\n" +
                "    \"level\": null\n" +
                "  }\n" +
                "}";
        ProcessingMessage securityLevelValidationErrorMessage = jsonSecurityDetailsValidationService.validateSecurityLevel(fromString(json));
        assertThat(securityLevelValidationErrorMessage, is(notNullValue()));
        verify(messageSource).getMessage(SECURITY_LEVEL_IS_NULL_KEY, null, Locale.getDefault());
    }

    @Test
    public void securityLevelMustMatchSecurityClass() {
        String json = "{\n" +
                "  \"security\": " +
                "  {\n" +
                "    \"standard\": \"ISKE\",\n" +
                "    \"class\": \"K1T2S1\",\n" +
                "    \"level\": \"H\"\n" +
                "  }\n" +
                "}";
        ProcessingMessage securityLevelValidationErrorMessage = jsonSecurityDetailsValidationService.validateSecurityLevel(fromString(json));
        assertThat(securityLevelValidationErrorMessage, is(notNullValue()));
        verify(messageSource).getMessage(LEVEL_DOES_NOT_MATCH_CLASS_KEY, null, Locale.getDefault());
    }

    @Test
    public void latestAuditResolutionMustBeNullWhenLatestAuditDateIsNull() {
        String json = "{\n" +
                "  \"security\": " +
                "  {\n" +
                "    \"latest_audit_date\": null,\n" +
                "    \"latest_audit_resolution\": \"Auditeeritud märkuste või soovitustega\"\n" +
                "  }\n" +
                "}";
        ProcessingMessage latestAuditResolutionValidationErrorMessage =
                jsonSecurityDetailsValidationService.validateLatestAuditResolution(fromString(json));
        assertThat(latestAuditResolutionValidationErrorMessage, is(notNullValue()));
        verify(messageSource).getMessage(LATEST_AUDIT_RESOLUTION_IS_NOT_NULL_KEY, null, Locale.getDefault());
    }

    @Test
    public void latestAuditResolutionMustNotBeNullWhenLatestAuditDateIsNotNull() {
        String json = "{\n" +
                "  \"security\": " +
                "  {\n" +
                "    \"latest_audit_date\": \"2017-11-13T17:15:55.002+02:00\",\n" +
                "    \"latest_audit_resolution\": null\n" +
                "  }\n" +
                "}";
        ProcessingMessage latestAuditResolutionValidationErrorMessage =
                jsonSecurityDetailsValidationService.validateLatestAuditResolution(fromString(json));
        assertThat(latestAuditResolutionValidationErrorMessage, is(notNullValue()));
        verify(messageSource).getMessage(LATEST_AUDIT_RESOLUTION_IS_NULL_KEY, null, Locale.getDefault());
    }

    @Test
    public void latestAuditResolutionValueIsNotAllowed() {
        String json = "{\n" +
                "  \"security\": " +
                "  {\n" +
                "    \"latest_audit_date\": \"2017-11-13T17:15:55.002+02:00\",\n" +
                "    \"latest_audit_resolution\": \"This is not allowed\"\n" +
                "  }\n" +
                "}";
        ProcessingMessage latestAuditResolutionValidationErrorMessage =
                jsonSecurityDetailsValidationService.validateLatestAuditResolution(fromString(json));
        assertThat(latestAuditResolutionValidationErrorMessage, is(notNullValue()));
        verify(messageSource).getMessage(LATEST_AUDIT_RESOLUTION_VALUE_IS_NOT_ALLOWED_KEY, null, Locale.getDefault());
    }

    private JsonNode fromString(String json) {
        try {
            return JsonLoader.fromString(json);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
