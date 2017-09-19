package ee.ria.riha.service;

import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import org.junit.Before;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Valentin Suhnjov
 */
public class JsonValidationServiceTest {

    private JsonValidationService infoSystemValidationService;

    @Before
    public void setUp() throws IOException {
        this.infoSystemValidationService = new JsonValidationService(
                JsonLoader.fromResource("/test_infosystem_schema.json"));
    }

    @Test(expected = JsonValidationServiceException.class)
    public void throwsOnEmptyJson() {
        infoSystemValidationService.validate("");
    }

    @Test
    public void successfullyValidatesCorrectJson() {
        //language=JSON
        String json = "{\n" +
                "  \"name\": \"test\",\n" +
                "  \"uuid\": \"00000000-0000-0000-0000-000000000000\"\n" +
                "}";

        ProcessingReport report = infoSystemValidationService.validate(json, false);

        assertThat(report.isSuccess(), equalTo(true));
    }

    @Test
    public void catchesMissingValues() {
        //language=JSON
        String json = "{\n" +
                "  \"name\": \"some name\"\n" +
                "}";
        ProcessingReport report = infoSystemValidationService.validate(json, false);

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
    public void catchesWronglyFormatValues() {
        //language=JSON
        String json = "{\n" +
                "  \"name\": \"asd\",\n" +
                "  \"uuid\": \"00000000-0000-0000-0000-000000000000\",\n" +
                "  \"sub-item\": {\n" +
                "    \"timestamp\": \"not-a-timestamp\"\n" +
                "  }\n" +
                "}";
        ProcessingReport report = infoSystemValidationService.validate(json, false);

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
    public void catchesErrorsInRegexPatternValidatedFields() {
        //language=JSON
        String json = "{\n" +
                "  \"name\": \"regex validation test\",\n" +
                "  \"uuid\": \"00000000-0000-0000-0000-000000000000\",\n" +
                "  \"short_name\": \"underscores_are_not_valid\"\n" +
                "}";

        ProcessingReport report = infoSystemValidationService.validate(json, false);

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

        ProcessingReport report = infoSystemValidationService.validate(json, false);

        assertThat(report.isSuccess(), equalTo(true));
    }

}