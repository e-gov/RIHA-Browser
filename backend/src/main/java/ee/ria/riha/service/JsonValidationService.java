package ee.ria.riha.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import java.io.IOException;

/**
 * Service for JSON validity check against JSON schema.
 *
 * @author Valentin Suhnjov
 */
public class JsonValidationService {

    private JsonSchema schema;

    public JsonValidationService(JsonNode schema) {
        try {
            this.schema = JsonSchemaFactory.byDefault().getJsonSchema(schema);
        } catch (ProcessingException e) {
            throw new JsonValidationServiceException("Error initializing validating schema", e);
        }
    }

    /**
     * Validates given JSON against schema. Will throw {@link JsonValidationException} if required or return
     * {@link ProcessingReport} otherwise.
     *
     * @param json                  validated json
     * @param exceptionMustBeThrown exception throwing flag
     * @return successful {@link ProcessingReport} or unsuccessful one if error throwing flag is false
     */
    public ProcessingReport validate(String json, boolean exceptionMustBeThrown) {
        try {
            JsonNode jsonNode = JsonLoader.fromString(json);
            ProcessingReport report = schema.validate(jsonNode, true);

            if (exceptionMustBeThrown && !report.isSuccess()) {
                throw new JsonValidationException(report);
            }

            return report;
        } catch (IOException | ProcessingException e) {
            throw new JsonValidationServiceException("Could not validate json", e);
        }
    }

    /**
     * Convenience method for {@link #validate(String, boolean)} that throws validation exception.
     *
     * @param json validated json
     * @return successful {@link ProcessingReport}
     */
    public ProcessingReport validate(String json) {
        return validate(json, true);
    }

}
