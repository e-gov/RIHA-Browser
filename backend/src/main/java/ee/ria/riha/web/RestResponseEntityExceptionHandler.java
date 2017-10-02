package ee.ria.riha.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import ee.ria.riha.service.JsonValidationException;
import ee.ria.riha.service.ValidationException;
import ee.ria.riha.web.model.ValidationErrorModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

/**
 * @author Valentin Suhnjov
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(JsonValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<JsonNode>> handleJsonValidationException(JsonValidationException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessages().stream()
                              .map(ProcessingMessage::asJson)
                              .collect(toList()));
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationErrorModel> handleValidationException(ValidationException e) {
        ValidationErrorModel model = new ValidationErrorModel();

        model.setCode(e.getCode());
        model.setArgs(e.getArgs());
        model.setMessage(messageSource.getMessage(e.getCode(), e.getArgs(), Locale.getDefault()));

        return ResponseEntity
                .badRequest()
                .body(model);
    }
}
