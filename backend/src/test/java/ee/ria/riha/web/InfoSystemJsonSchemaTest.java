package ee.ria.riha.web;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.service.JsonSecurityDetailsValidationService;
import ee.ria.riha.service.JsonValidationException;
import ee.ria.riha.service.JsonValidationService;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.WARN)
@ExtendWith(MockitoExtension.class)
public class InfoSystemJsonSchemaTest {

  private UUID uuid = UUID.fromString("5d68f510-86f0-4e05-9502-c761bbebe6be");
  private JsonValidationService jsonValidationService;
  private InfoSystem infoSystem = new InfoSystem();
  private ObjectMapper objectMapper = new ObjectMapper();

  @Mock
  private JsonSecurityDetailsValidationService jsonSecurityDetailsValidationService =
      new JsonSecurityDetailsValidationService();

  @BeforeEach
  public void setUp() throws IOException {
    JsonNode schemaNode = loadFromResource("/infosystem_schema.json");
    this.jsonValidationService = new JsonValidationService(schemaNode);
    jsonValidationService.setJsonSecurityDetailsValidationService(
        jsonSecurityDetailsValidationService);

    infoSystem.setUuid(uuid);
    infoSystem.setFullName("Test info system");
    infoSystem.setShortName("t1");
    infoSystem.setOwnerName("owner");
    infoSystem.setOwnerCode("1234");
    infoSystem.setPurpose("testing");

    when(jsonSecurityDetailsValidationService.isNecessaryToValidateSecurityDetails(any()))
        .thenReturn(false);
  }

  private JsonNode loadFromResource(String resourcePath) throws IOException {
    try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
      if (is == null) {
        throw new IOException("Resource not found: " + resourcePath);
      }
      return objectMapper.readTree(is);
    }
  }

  @Test
  public void infoSystemSuccessfullyValidates() {
    jsonValidationService.validate(infoSystem.getJsonContent());
  }

  @Test
  public void shortNameMustContainAtLeastOneCharacter() {
    assertThrows(
        JsonValidationException.class,
        () -> {
          infoSystem.setShortName("");
          jsonValidationService.validate(infoSystem.getJsonContent());
        });
  }

  @Test
  public void shortNameValidationFailsWhenItIsValidUuid() {
    assertThrows(
        JsonValidationException.class,
        () -> {
          infoSystem.setShortName(uuid.toString());
          jsonValidationService.validate(infoSystem.getJsonContent());
        });
  }

  @Test
  public void shortNameValidatesWhenItIsUuidLike() {
    // UUID.fromString() will work just fine but validation must fail
    infoSystem.setShortName("1-1-1-1-1");
    jsonValidationService.validate(infoSystem.getJsonContent());
  }

  @Test
  public void shortNameValidatedWhenItIsPrefixedUuid() {
    infoSystem.setShortName("prefix" + uuid.toString());
    jsonValidationService.validate(infoSystem.getJsonContent());
  }

  @Test
  public void shortNameValidatesWhenItIsSuffixedUuid() {
    infoSystem.setShortName(uuid.toString() + "suffix");
    jsonValidationService.validate(infoSystem.getJsonContent());
  }

  @Test
  public void shortNameValidatesWhenItContainsTildeAndDiaeresisCharacters() {
    infoSystem.setShortName("õÕäÄöÖüÜ");
    jsonValidationService.validate(infoSystem.getJsonContent());
  }

  @Test
  public void shortNameValidatedWhenItContainsNumbers() {
    infoSystem.setShortName("1234");
    jsonValidationService.validate(infoSystem.getJsonContent());
  }

  @Test
  public void shortNameValidatesWhenItContainsDashes() {
    infoSystem.setShortName("dashed-short-name");
    jsonValidationService.validate(infoSystem.getJsonContent());
  }

  @Test
  public void shortNameValidatesWhenItContainsDots() {
    infoSystem.setShortName("dotted.short.name");
    jsonValidationService.validate(infoSystem.getJsonContent());
  }

  @Test
  public void shortNameValidationFailsWhenItContainsIllegalCharacter() {
    assertThrows(
        JsonValidationException.class,
        () -> {
          infoSystem.setShortName("in>alid");
          jsonValidationService.validate(infoSystem.getJsonContent());
        });
  }

  @Test
  public void failsWhenDocumentTypeIsNull() {
    assertThrows(
        JsonValidationException.class,
        () -> {
          ((ArrayNode) infoSystem.getJsonContent().withArray("documents"))
              .add(createDocument("document", "document_url", null));
          jsonValidationService.validate(infoSystem.getJsonContent());
        });
  }

  @Test
  public void successfullyValidatesJsonWhenDocumentTypeValueIsMissing() {
    // type may be missing for backward compatibility, but may not be null
    ((ArrayNode) infoSystem.getJsonContent().withArray("documents"))
        .add(createDocument("document", "document_url"));
    jsonValidationService.validate(infoSystem.getJsonContent());
  }

  @Test
  public void successfullyValidatesJsonWhenDocumentTypeValueBelongsToEnumValues() {
    ((ArrayNode) infoSystem.getJsonContent().withArray("documents"))
        .add(createDocument("document", "document_url", "DOC_TYPE_OPEN_DATA"));
    jsonValidationService.validate(infoSystem.getJsonContent());
  }

  @Test
  public void failsWhenDocumentTypeValueIsNotNullAndDoesNotBelongToEnumValues() {
    assertThrows(
        JsonValidationException.class,
        () -> {
          ((ArrayNode) infoSystem.getJsonContent().withArray("documents"))
              .add(createDocument("document", "document_url", "INVALID_TYPE"));
          jsonValidationService.validate(infoSystem.getJsonContent());
        });
  }

  @Test
  public void failsWhenLegislationTypeIsNull() {
    assertThrows(
        JsonValidationException.class,
        () -> {
          ((ArrayNode) infoSystem.getJsonContent().withArray("legislations"))
              .add(createDocument("legislation", "legislation_url", null));
          jsonValidationService.validate(infoSystem.getJsonContent());
        });
  }

  @Test
  public void successfullyValidatesJsonWhenLegislationTypeValueIsMissing() {
    // type may be missing for backward compatibility, but may not be null
    ((ArrayNode) infoSystem.getJsonContent().withArray("legislations"))
        .add(createDocument("legislation", "legislation_url"));
    jsonValidationService.validate(infoSystem.getJsonContent());
  }

  @Test
  public void successfullyValidatesJsonWhenLegislationTypeValueBelongsToEnumValues() {
    ((ArrayNode) infoSystem.getJsonContent().withArray("legislations"))
        .add(createDocument("legislation", "legislation_url", "LEGAL_TYPE_DRAFT_STATUTE_NOTE"));
    jsonValidationService.validate(infoSystem.getJsonContent());
  }

  @Test
  public void failsWhenLegislationTypeValueIsNotNullAndDoesNotBelongToEnumValues() {
    assertThrows(
        JsonValidationException.class,
        () -> {
          ((ArrayNode) infoSystem.getJsonContent().withArray("legislations"))
              .add(createDocument("legislation", "legislation_url", "INVALID_TYPE"));
          jsonValidationService.validate(infoSystem.getJsonContent());
        });
  }

  private JsonNode createDocument(String name, String url) {
    return JsonNodeFactory.instance.objectNode().put("name", name).put("url", url);
  }

  private JsonNode createDocument(String name, String url, String type) {
    return JsonNodeFactory.instance
        .objectNode()
        .put("name", name)
        .put("url", url)
        .put("type", type);
  }
}
