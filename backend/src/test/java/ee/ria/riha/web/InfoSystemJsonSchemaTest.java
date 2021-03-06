package ee.ria.riha.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.fge.jackson.JsonLoader;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.service.JsonSecurityDetailsValidationService;
import ee.ria.riha.service.JsonValidationException;
import ee.ria.riha.service.JsonValidationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InfoSystemJsonSchemaTest {

    private UUID uuid = UUID.fromString("5d68f510-86f0-4e05-9502-c761bbebe6be");
    private JsonValidationService jsonValidationService;
    private InfoSystem infoSystem = new InfoSystem();

    @Mock
    private JsonSecurityDetailsValidationService jsonSecurityDetailsValidationService = new JsonSecurityDetailsValidationService();

    @Before
    public void setUp() throws IOException {
        this.jsonValidationService = new JsonValidationService(
                JsonLoader.fromResource("/infosystem_schema.json"));
        jsonValidationService.setJsonSecurityDetailsValidationService(jsonSecurityDetailsValidationService);

        infoSystem.setUuid(uuid);
        infoSystem.setFullName("Test info system");
        infoSystem.setShortName("t1");
        infoSystem.setOwnerName("owner");
        infoSystem.setOwnerCode("1234");
        infoSystem.setPurpose("testing");

        when(jsonSecurityDetailsValidationService.isNecessaryToValidateSecurityDetails(any())).thenReturn(false);
    }

    @Test
    public void infoSystemSuccessfullyValidates() {
        jsonValidationService.validate(infoSystem.getJsonContent());
    }

    @Test(expected = JsonValidationException.class)
    public void shortNameMustContainAtLeastOneCharacter() {
        infoSystem.setShortName("");
        jsonValidationService.validate(infoSystem.getJsonContent());
    }

    @Test(expected = JsonValidationException.class)
    public void shortNameValidationFailsWhenItIsValidUuid() {
        infoSystem.setShortName(uuid.toString());
        jsonValidationService.validate(infoSystem.getJsonContent());
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

    @Test(expected = JsonValidationException.class)
    public void shortNameValidationFailsWhenItContainsIllegalCharacter() {
        infoSystem.setShortName("in>alid");
        jsonValidationService.validate(infoSystem.getJsonContent());
    }

    @Test(expected = JsonValidationException.class)
    public void failsWhenDocumentTypeIsNull() {
        ((ArrayNode) infoSystem.getJsonContent().withArray("documents"))
                .add(createDocument("document", "document_url", null));
        jsonValidationService.validate(infoSystem.getJsonContent());
    }

    @Test
    public void successfullyValidatesJsonWhenDocumentTypeValueIsMissing() {
        //type may be missing for backward compatibility, but may not be null
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

    @Test(expected = JsonValidationException.class)
    public void failsWhenDocumentTypeValueIsNotNullAndDoesNotBelongToEnumValues() {
        ((ArrayNode) infoSystem.getJsonContent().withArray("documents"))
                .add(createDocument("document", "document_url", "INVALID_TYPE"));
        jsonValidationService.validate(infoSystem.getJsonContent());
    }

    @Test(expected = JsonValidationException.class)
    public void failsWhenLegislationTypeIsNull() {
        ((ArrayNode) infoSystem.getJsonContent().withArray("legislations"))
                .add(createDocument("legislation", "legislation_url", null));
        jsonValidationService.validate(infoSystem.getJsonContent());
    }

    @Test
    public void successfullyValidatesJsonWhenLegislationTypeValueIsMissing() {
        //type may be missing for backward compatibility, but may not be null
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

    @Test(expected = JsonValidationException.class)
    public void failsWhenLegislationTypeValueIsNotNullAndDoesNotBelongToEnumValues() {
        ((ArrayNode) infoSystem.getJsonContent().withArray("legislations"))
                .add(createDocument("legislation", "legislation_url", "INVALID_TYPE"));
        jsonValidationService.validate(infoSystem.getJsonContent());
    }

    private JsonNode createDocument(String name, String url) {
        return JsonNodeFactory.instance.objectNode()
                .put("name", name)
                .put("url", url);
    }

    private JsonNode createDocument(String name, String url, String type) {
        return JsonNodeFactory.instance.objectNode()
                .put("name", name)
                .put("url", url)
                .put("type", type);
    }
}