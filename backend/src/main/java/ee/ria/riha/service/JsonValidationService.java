package ee.ria.riha.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Service for JSON validity check against JSON schema.
 *
 * @author Valentin Suhnjov
 */
public class JsonValidationService {

    private JsonSchema schema;
    private JsonSecurityDetailsValidationService jsonSecurityDetailsValidationService;

    public JsonValidationService(JsonNode schema) {
        try {
            this.schema = JsonSchemaFactory.byDefault().getJsonSchema(schema);
        } catch (ProcessingException e) {
            throw new IllegalBrowserStateException("Error initializing validating schema", e);
        }
    }

    /**
     * Convenience method for {@link #validate(JsonNode, boolean)} that throws validation exception.
     *
     * @param jsonNode validated json
     * @return successful {@link ProcessingReport}
     */
    public ProcessingReport validate(JsonNode jsonNode) {
        return validate(jsonNode, true);
    }

    /**
     * Validates given JSON against schema. Will throw {@link JsonValidationException} if required or return {@link
     * ProcessingReport} otherwise.
     *
     * @param jsonNode              validated json
     * @param exceptionMustBeThrown exception throwing flag
     * @return successful {@link ProcessingReport} or unsuccessful one if error throwing flag is false
     */
    public ProcessingReport validate(JsonNode jsonNode, boolean exceptionMustBeThrown) {
        try {
            ProcessingReport report = schema.validate(jsonNode, true);

            if (report.isSuccess() && jsonSecurityDetailsValidationService.isNecessaryToValidateSecurityDetails(jsonNode)) {
                List<ProcessingMessage> securityDetailsValidationErrorMessages = jsonSecurityDetailsValidationService.validate(jsonNode);
                if (!securityDetailsValidationErrorMessages.isEmpty()) {
                    for (ProcessingMessage errorMessage : securityDetailsValidationErrorMessages) {
                        report.error(errorMessage);
                    }
                }
            }

            if (exceptionMustBeThrown && !report.isSuccess()) {
                throw new JsonValidationException(report);
            }

            return report;
        } catch (ProcessingException e) {
            throw new IllegalBrowserStateException("Could not validate json", e);
        }
    }

    @Autowired
    public void setJsonSecurityDetailsValidationService(JsonSecurityDetailsValidationService jsonSecurityDetailsValidationService) {
        this.jsonSecurityDetailsValidationService = jsonSecurityDetailsValidationService;
    }
}
