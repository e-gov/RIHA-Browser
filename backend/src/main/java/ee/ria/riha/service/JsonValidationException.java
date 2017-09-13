package ee.ria.riha.service;

import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;

import java.util.ArrayList;
import java.util.List;

/**
 * Thrown when Information System data does not comply with schema.
 *
 * @author Valentin Suhnjov
 */
public class JsonValidationException extends RuntimeException {

    private final ProcessingReport processingReport;

    public JsonValidationException(ProcessingReport processingReport) {
        this.processingReport = processingReport;
    }

    public List<ProcessingMessage> getMessages() {
        ArrayList<ProcessingMessage> messages = new ArrayList<>();
        if (processingReport != null) {
            processingReport.forEach(messages::add);
        }

        return messages;
    }
}
