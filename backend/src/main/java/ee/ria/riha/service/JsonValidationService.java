package ee.ria.riha.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Iterator;

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
            JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
            this.schema = factory.getSchema(schema);
        } catch (Exception e) {
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
            // Use networknt validation
            Set<ValidationMessage> errors = schema.validate(jsonNode);
            ProcessingReport report = new ProcessingReport(errors);

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
        } catch (Exception e) {
            throw new IllegalBrowserStateException("Could not validate json", e);
        }
    }

    @Autowired
    public void setJsonSecurityDetailsValidationService(JsonSecurityDetailsValidationService jsonSecurityDetailsValidationService) {
        this.jsonSecurityDetailsValidationService = jsonSecurityDetailsValidationService;
    }

    /**
     * Wrapper class to maintain API compatibility with old com.github.fge.jsonschema.core.report.ProcessingReport
     */
    public static class ProcessingReport implements Iterable<ProcessingMessage> {
        private final List<ProcessingMessage> messages;
        private boolean success;

        public ProcessingReport(Set<ValidationMessage> networkntMessages) {
            this.messages = new ArrayList<>();
            this.success = networkntMessages.isEmpty();
            
            for (ValidationMessage msg : networkntMessages) {
                this.messages.add(new ProcessingMessage(msg));
            }
        }

        public ProcessingReport() {
            this.messages = new ArrayList<>();
            this.success = true;
        }

        public boolean isSuccess() {
            return success && messages.stream().allMatch(ProcessingMessage::isSuccess);
        }

        public void error(ProcessingMessage message) {
            this.messages.add(message);
            this.success = false;
        }

        @Override
        public Iterator<ProcessingMessage> iterator() {
            return messages.iterator();
        }
    }

    /**
     * Wrapper class to maintain API compatibility with old com.github.fge.jsonschema.core.report.ProcessingMessage
     */
    public static class ProcessingMessage {
        private JsonNode jsonNode;
        private final boolean success;

        public ProcessingMessage(ValidationMessage networkntMessage) {
            ObjectMapper mapper = new ObjectMapper();
            this.success = false;
            try {
                // Create a simple JSON structure for the validation message
                com.fasterxml.jackson.databind.node.ObjectNode messageNode = mapper.createObjectNode();
                messageNode.put("message", networkntMessage.getMessage());
                this.jsonNode = messageNode;
            } catch (Exception e) {
                // Ultimate fallback
                this.jsonNode = mapper.createObjectNode();
                ((com.fasterxml.jackson.databind.node.ObjectNode) this.jsonNode).put("message", "Validation error");
            }
        }

        public ProcessingMessage() {
            this.success = true;
            this.jsonNode = new ObjectMapper().createObjectNode();
        }

        public JsonNode asJson() {
            return jsonNode;
        }

        public boolean isSuccess() {
            return success;
        }

        public ProcessingMessage put(String key, String value) {
            if (jsonNode instanceof com.fasterxml.jackson.databind.node.ObjectNode) {
                ((com.fasterxml.jackson.databind.node.ObjectNode) jsonNode).put(key, value);
            }
            return this;
        }
    }
}
