package ee.ria.riha.service;

import ee.ria.riha.service.JsonValidationService.ProcessingMessage;
import ee.ria.riha.service.JsonValidationService.ProcessingReport;
import java.util.ArrayList;
import java.util.List;

/**
 * Thrown when Information System data does not comply with schema.
 *
 * @author Valentin Suhnjov
 */
public class JsonValidationException extends BrowserException {

  private final transient ProcessingReport processingReport;

  JsonValidationException(ProcessingReport processingReport) {
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
