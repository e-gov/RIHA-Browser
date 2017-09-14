package ee.ria.riha.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import ee.ria.riha.service.JsonValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Valentin Suhnjov
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(JsonValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<JsonNode>> handleJsonValidationException(JsonValidationException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessages().stream()
                              .map(ProcessingMessage::asJson)
                              .collect(toList()));
    }

}
