package ee.ria.riha.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.ria.riha.service.JsonValidationService.ProcessingMessage;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.MessageSource;

@MockitoSettings(strictness = Strictness.WARN)
@ExtendWith(MockitoExtension.class)
public class JsonSecurityDetailsValidationServiceTest {

  private static final String DEFAULT_VALIDATION_ERROR_MESSAGE = "securityDetailsValidationError";
  // validation process consists of 3 parts where each part generates maximum 1 validation error
  // message
  // even though there may be more of them
  private static final int MAX_AMOUNT_OF_VALIDATION_ERRORS = 3;

  private static final String SECURITY_CLASS_IS_NULL_KEY =
      "validation.system.json.securityClass.isNullWhenStandardIsISKE";
  private static final String SECURITY_CLASS_IS_NOT_NULL_KEY =
      "validation.system.json.securityClass.isNotNullWhenStandardIsNotISKE";
  private static final String SECURITY_LEVEL_IS_NULL_KEY =
      "validation.system.json.securityLevel.isNullWhenStandardIsISKE";
  private static final String SECURITY_LEVEL_IS_NOT_NULL_KEY =
      "validation.system.json.securityLevel.isNotNullWhenStandardIsNotISKE";
  private static final String LEVEL_DOES_NOT_MATCH_CLASS_KEY =
      "validation.system.json.securityLevel.doesNotMatchSecurityClass";
  private static final String LATEST_AUDIT_RESOLUTION_IS_NULL_KEY =
      "validation.system.json.latestAuditResolution.isNullWhenLatestAuditDateIsNotNull";
  private static final String LATEST_AUDIT_RESOLUTION_IS_NOT_NULL_KEY =
      "validation.system.json.latestAuditResolution.isNotNullWhenLatestAuditDateIsNull";
  private static final String LATEST_AUDIT_RESOLUTION_VALUE_IS_NOT_ALLOWED_KEY =
      "validation.system.json.latestAuditResolution.valueIsNotAllowed";

  @Mock private MessageSource messageSource;
  @InjectMocks private JsonSecurityDetailsValidationService jsonSecurityDetailsValidationService;
  private ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  public void setUp() {
    when(messageSource.getMessage(any(String.class), any(), any(Locale.class)))
        .thenReturn(DEFAULT_VALIDATION_ERROR_MESSAGE);
  }

  @Test
  public void successfullyValidatesCorrectJson() {
    String json =
        """
        {
          "security": \
          {
            "standard": "ISKE",
            "class": "K1T2S1",
            "level": "M",
            "latest_audit_date": "2017-11-13T17:15:55.002+02:00",
            "latest_audit_resolution": "PASSED_WITH_REMARKS"
          }
        }\
        """;
    List<ProcessingMessage> validationErrorMessages =
        jsonSecurityDetailsValidationService.validate(fromString(json));
    assertThat(validationErrorMessages, is(emptyCollectionOf(ProcessingMessage.class)));
  }

  @Test
  public void catchesMaxAmountOfValidationErrorMessages() {
    String json =
        """
        {
          "security": \
          {
            "standard": "ISKE",
            "class": null,
            "level": null,
            "latest_audit_date": null,
            "latest_audit_resolution": "PASSED_WITH_REMARKS"
          }
        }\
        """;
    List<ProcessingMessage> validationErrorMessages =
        jsonSecurityDetailsValidationService.validate(fromString(json));
    assertThat(validationErrorMessages.size(), is(equalTo(MAX_AMOUNT_OF_VALIDATION_ERRORS)));
    verify(messageSource).getMessage(SECURITY_CLASS_IS_NULL_KEY, null, Locale.getDefault());
    verify(messageSource).getMessage(SECURITY_LEVEL_IS_NULL_KEY, null, Locale.getDefault());
    verify(messageSource)
        .getMessage(LATEST_AUDIT_RESOLUTION_IS_NOT_NULL_KEY, null, Locale.getDefault());
  }

  @Test
  public void securityClassMustBeNullWhenStandardIsNotISKE() {
    String json =
        """
        {
          "security": \
          {
            "standard": "This is not ISKE",
            "class": "K1T2S1"
          }
        }\
        """;
    ProcessingMessage securityClassValidationErrorMessage =
        jsonSecurityDetailsValidationService.validateSecurityClass(fromString(json));
    assertThat(securityClassValidationErrorMessage, is(notNullValue()));
    verify(messageSource).getMessage(SECURITY_CLASS_IS_NOT_NULL_KEY, null, Locale.getDefault());
  }

  @Test
  public void securityClassMustNotBeNullWhenStandardIsISKE() {
    String json =
        """
        {
          "security": \
          {
            "standard": "ISKE",
            "class": null
          }
        }\
        """;
    ProcessingMessage securityClassValidationErrorMessage =
        jsonSecurityDetailsValidationService.validateSecurityClass(fromString(json));
    assertThat(securityClassValidationErrorMessage, is(notNullValue()));
    verify(messageSource).getMessage(SECURITY_CLASS_IS_NULL_KEY, null, Locale.getDefault());
  }

  @Test
  public void securityLevelMustBeNullWhenStandardIsNotISKE() {
    String json =
        """
        {
          "security": \
          {
            "standard": "This is not ISKE",
            "class": "K1T2S1",
            "level": "M"
          }
        }\
        """;
    ProcessingMessage securityLevelValidationErrorMessage =
        jsonSecurityDetailsValidationService.validateSecurityLevel(fromString(json));
    assertThat(securityLevelValidationErrorMessage, is(notNullValue()));
    verify(messageSource).getMessage(SECURITY_LEVEL_IS_NOT_NULL_KEY, null, Locale.getDefault());
  }

  @Test
  public void securityLevelMustNotBeNullWhenStandardIsISKE() {
    String json =
        """
        {
          "security": \
          {
            "standard": "ISKE",
            "class": "K1T2S1",
            "level": null
          }
        }\
        """;
    ProcessingMessage securityLevelValidationErrorMessage =
        jsonSecurityDetailsValidationService.validateSecurityLevel(fromString(json));
    assertThat(securityLevelValidationErrorMessage, is(notNullValue()));
    verify(messageSource).getMessage(SECURITY_LEVEL_IS_NULL_KEY, null, Locale.getDefault());
  }

  @Test
  public void securityLevelMustMatchSecurityClass() {
    String json =
        """
        {
          "security": \
          {
            "standard": "ISKE",
            "class": "K1T2S1",
            "level": "H"
          }
        }\
        """;
    ProcessingMessage securityLevelValidationErrorMessage =
        jsonSecurityDetailsValidationService.validateSecurityLevel(fromString(json));
    assertThat(securityLevelValidationErrorMessage, is(notNullValue()));
    verify(messageSource).getMessage(LEVEL_DOES_NOT_MATCH_CLASS_KEY, null, Locale.getDefault());
  }

  @Test
  public void latestAuditResolutionMustBeNullWhenLatestAuditDateIsNull() {
    String json =
        """
        {
          "security": \
          {
            "latest_audit_date": null,
            "latest_audit_resolution": "PASSED_WITH_REMARKS"
          }
        }\
        """;
    ProcessingMessage latestAuditResolutionValidationErrorMessage =
        jsonSecurityDetailsValidationService.validateLatestAuditResolution(fromString(json));
    assertThat(latestAuditResolutionValidationErrorMessage, is(notNullValue()));
    verify(messageSource)
        .getMessage(LATEST_AUDIT_RESOLUTION_IS_NOT_NULL_KEY, null, Locale.getDefault());
  }

  @Test
  public void latestAuditResolutionMustNotBeNullWhenLatestAuditDateIsNotNull() {
    String json =
        """
        {
          "security": \
          {
            "latest_audit_date": "2017-11-13T17:15:55.002+02:00",
            "latest_audit_resolution": null
          }
        }\
        """;
    ProcessingMessage latestAuditResolutionValidationErrorMessage =
        jsonSecurityDetailsValidationService.validateLatestAuditResolution(fromString(json));
    assertThat(latestAuditResolutionValidationErrorMessage, is(notNullValue()));
    verify(messageSource)
        .getMessage(LATEST_AUDIT_RESOLUTION_IS_NULL_KEY, null, Locale.getDefault());
  }

  @Test
  public void latestAuditResolutionValueIsNotAllowed() {
    String json =
        """
        {
          "security": \
          {
            "latest_audit_date": "2017-11-13T17:15:55.002+02:00",
            "latest_audit_resolution": "This is not allowed"
          }
        }\
        """;
    ProcessingMessage latestAuditResolutionValidationErrorMessage =
        jsonSecurityDetailsValidationService.validateLatestAuditResolution(fromString(json));
    assertThat(latestAuditResolutionValidationErrorMessage, is(notNullValue()));
    verify(messageSource)
        .getMessage(LATEST_AUDIT_RESOLUTION_VALUE_IS_NOT_ALLOWED_KEY, null, Locale.getDefault());
  }

  private JsonNode fromString(String json) {
    try {
      return objectMapper.readTree(json);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
