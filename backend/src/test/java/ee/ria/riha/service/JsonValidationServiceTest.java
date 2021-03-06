package ee.ria.riha.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Valentin Suhnjov
 */
@RunWith(MockitoJUnitRunner.class)
public class JsonValidationServiceTest {

    @Mock
    private JsonSecurityDetailsValidationService jsonSecurityDetailsValidationService = new JsonSecurityDetailsValidationService();
    private JsonValidationService infoSystemValidationService;

    @Before
    public void setUp() throws IOException {
        this.infoSystemValidationService = new JsonValidationService(
                JsonLoader.fromResource("/test_infosystem_schema.json"));
        infoSystemValidationService.setJsonSecurityDetailsValidationService(jsonSecurityDetailsValidationService);

        when(jsonSecurityDetailsValidationService.isNecessaryToValidateSecurityDetails(any())).thenReturn(false);
    }

    @Test
    public void successfullyValidatesCorrectJson() {
        //language=JSON
        String json = "{\n" +
                "  \"name\": \"test\",\n" +
                "  \"uuid\": \"00000000-0000-0000-0000-000000000000\"\n" +
                "}";

        ProcessingReport report = infoSystemValidationService.validate(fromString(json), false);

        assertThat(report.isSuccess(), equalTo(true));
    }

    private JsonNode fromString(String json) {
        try {
            return JsonLoader.fromString(json);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void catchesMissingValues() throws JSONException {
        //language=JSON
        String json = "{\n" +
                "  \"name\": \"some name\"\n" +
                "}";
        ProcessingReport report = infoSystemValidationService.validate(fromString(json), false);

        assertThat(report.isSuccess(), equalTo(false));
        for (ProcessingMessage message : report) {
            JSONAssert.assertEquals("{\n" +
                            "  \"keyword\": \"required\",\n" +
                            "  \"required\": [\n" +
                            "    \"name\",\n" +
                            "    \"uuid\"\n" +
                            "  ],\n" +
                            "  \"missing\": [\n" +
                            "    \"uuid\"\n" +
                            "  ]\n" +
                            "}",
                    message.asJson().toString(),
                    false);
        }
    }

    @Test
    public void catchesWronglyFormatValues() throws JSONException {
        //language=JSON
        String json = "{\n" +
                "  \"name\": \"asd\",\n" +
                "  \"uuid\": \"00000000-0000-0000-0000-000000000000\",\n" +
                "  \"sub-item\": {\n" +
                "    \"timestamp\": \"not-a-timestamp\"\n" +
                "  }\n" +
                "}";
        ProcessingReport report = infoSystemValidationService.validate(fromString(json), false);

        assertThat(report.isSuccess(), equalTo(false));
        for (ProcessingMessage message : report) {
            JSONAssert.assertEquals("{\n" +
                            "  \"keyword\": \"format\",\n" +
                            "  \"value\": \"not-a-timestamp\"\n" +
                            "}",
                    message.asJson().toString(),
                    false);
        }
    }

    @Test
    public void catchesErrorsInRegexPatternValidatedFields() throws JSONException {
        //language=JSON
        String json = "{\n" +
                "  \"name\": \"regex validation test\",\n" +
                "  \"uuid\": \"00000000-0000-0000-0000-000000000000\",\n" +
                "  \"short_name\": \"underscores_are_not_valid\"\n" +
                "}";

        ProcessingReport report = infoSystemValidationService.validate(fromString(json), false);

        assertThat(report.isSuccess(), equalTo(false));
        for (ProcessingMessage message : report) {
            JSONAssert.assertEquals("{\n" +
                    "  \"keyword\": \"pattern\",\n" +
                    "  \"string\": \"underscores_are_not_valid\"\n" +
                    "}", message.asJson().toString(), false);
        }
    }

    @Test
    public void successfullyValidatesRegexPatternValidatedFields() {
        //language=JSON
        String json = "{\n" +
                "  \"name\": \"regex validation test\",\n" +
                "  \"uuid\": \"00000000-0000-0000-0000-000000000000\",\n" +
                "  \"short_name\": \"all-these.VALUES-allowed-õÕäÄöÖüÜ-0123456789\"\n" +
                "}";

        ProcessingReport report = infoSystemValidationService.validate(fromString(json), false);

        assertThat(report.isSuccess(), equalTo(true));
    }

}