package ee.ria.riha.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.ria.riha.service.JsonValidationService.ProcessingMessage;
import ee.ria.riha.service.JsonValidationService.ProcessingReport;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Valentin Suhnjov
 */
@MockitoSettings(strictness = Strictness.WARN)
@ExtendWith(MockitoExtension.class)
public class JsonValidationServiceTest {

    @Mock
    private JsonSecurityDetailsValidationService jsonSecurityDetailsValidationService = new JsonSecurityDetailsValidationService();
    private JsonValidationService infoSystemValidationService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws IOException {
        JsonNode schemaNode = loadFromResource("/test_infosystem_schema.json");
        this.infoSystemValidationService = new JsonValidationService(schemaNode);
        infoSystemValidationService.setJsonSecurityDetailsValidationService(jsonSecurityDetailsValidationService);

        when(jsonSecurityDetailsValidationService.isNecessaryToValidateSecurityDetails(any())).thenReturn(false);
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
    public void successfullyValidatesCorrectJson() {
        //language=JSON
        String json = """
                {
                  "name": "test",
                  "uuid": "00000000-0000-0000-0000-000000000000"
                }\
                """;

        ProcessingReport report = infoSystemValidationService.validate(fromString(json), false);

        assertThat(report.isSuccess(), equalTo(true));
    }

    private JsonNode fromString(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void catchesMissingValues() throws JSONException {
        //language=JSON
        String json = """
                {
                  "name": "some name"
                }\
                """;
        ProcessingReport report = infoSystemValidationService.validate(fromString(json), false);

        assertThat(report.isSuccess(), equalTo(false));
        for (ProcessingMessage message : report) {
            JSONAssert.assertEquals("""
                            {
                              "keyword": "required",
                              "required": [
                                "name",
                                "uuid"
                              ],
                              "missing": [
                                "uuid"
                              ]
                            }\
                            """,
                    message.asJson().toString(),
                    false);
        }
    }

    @Test
    public void catchesWronglyFormatValues() throws JSONException {
        //language=JSON
        String json = """
                {
                  "name": "asd",
                  "uuid": "00000000-0000-0000-0000-000000000000",
                  "sub-item": {
                    "timestamp": "not-a-timestamp"
                  }
                }\
                """;
        ProcessingReport report = infoSystemValidationService.validate(fromString(json), false);

        assertThat(report.isSuccess(), equalTo(false));
        for (ProcessingMessage message : report) {
            JSONAssert.assertEquals("""
                            {
                              "keyword": "format",
                              "value": "not-a-timestamp"
                            }\
                            """,
                    message.asJson().toString(),
                    false);
        }
    }

    @Test
    public void catchesErrorsInRegexPatternValidatedFields() throws JSONException {
        //language=JSON
        String json = """
                {
                  "name": "regex validation test",
                  "uuid": "00000000-0000-0000-0000-000000000000",
                  "short_name": "underscores_are_not_valid"
                }\
                """;

        ProcessingReport report = infoSystemValidationService.validate(fromString(json), false);

        assertThat(report.isSuccess(), equalTo(false));
        for (ProcessingMessage message : report) {
            JSONAssert.assertEquals("""
                    {
                      "keyword": "pattern",
                      "string": "underscores_are_not_valid"
                    }\
                    """, message.asJson().toString(), false);
        }
    }

    @Test
    public void successfullyValidatesRegexPatternValidatedFields() {
        //language=JSON
        String json = """
                {
                  "name": "regex validation test",
                  "uuid": "00000000-0000-0000-0000-000000000000",
                  "short_name": "all-these.VALUES-allowed-õÕäÄöÖüÜ-0123456789"
                }\
                """;

        ProcessingReport report = infoSystemValidationService.validate(fromString(json), false);

        assertThat(report.isSuccess(), equalTo(true));
    }

}
