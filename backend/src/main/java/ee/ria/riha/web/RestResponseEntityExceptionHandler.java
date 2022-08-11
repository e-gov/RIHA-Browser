package ee.ria.riha.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import ee.ria.riha.client.StorageClientException;
import ee.ria.riha.client.StorageError;
import ee.ria.riha.client.StorageErrorCode;
import ee.ria.riha.service.*;
import ee.ria.riha.web.model.CodedBrowserExceptionModel;
import ee.ria.riha.web.model.SizeLimitExceptionModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.hasText;

/**
 * @author Valentin Suhnjov
 */
@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(JsonValidationException.class)
    public ResponseEntity<List<JsonNode>> handleJsonValidation(JsonValidationException e) {
        if (log.isTraceEnabled()) {
            log.trace("Handling JSON validation exception", e);
        }

        return ResponseEntity
                .badRequest()
                .body(e.getMessages().stream()
                        .map(ProcessingMessage::asJson)
                        .collect(toList()));
    }

    @ExceptionHandler(StorageClientException.class)
    public ResponseEntity<CodedBrowserExceptionModel> handleStorageClient(StorageClientException e) {
        if (log.isTraceEnabled()) {
            log.trace("Handling storage exception", e);
        }

        StorageError storageError = e.getStorageError();
        if (storageError == null) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createBrowserExceptionModel(e, "error.storage.unknown", null));
        }

        if (storageError.getErrcode() == StorageErrorCode.INPUT_NO_OBJECT_FOUND_WITH_GIVEN_ID) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(createBrowserExceptionModel(e, "error.storage.objectNotFound",
                            new Object[]{storageError.getErrtrace()}));
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createBrowserExceptionModel(e, "error.storage.unhandled",
                        new Object[]{storageError.getErrcode(), storageError.getErrmsg(), storageError.getErrtrace()}));
    }


    private CodedBrowserExceptionModel createBrowserExceptionModel(Throwable exception, String code, Object[] args) {
        CodedBrowserExceptionModel model = new CodedBrowserExceptionModel();
        model.setException(exception.getClass());
        model.setCode(code);
        model.setArgs(args);

        if (hasText(code)) {
            model.setMessage(messageSource.getMessage(code, args, code, Locale.getDefault()));
        } else {
            model.setMessage(exception.getMessage());
        }

        return model;
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<CodedBrowserExceptionModel> handleObjectNotFound(ObjectNotFoundException e) {
        if (log.isTraceEnabled()) {
            log.trace("Handling object not found exception", e);
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(createBrowserExceptionModel(e));
    }

    private CodedBrowserExceptionModel createBrowserExceptionModel(CodedBrowserException exception) {
        return createBrowserExceptionModel(exception, exception.getCode(), exception.getArgs());
    }

    @ExceptionHandler(BrowserException.class)
    public ResponseEntity<CodedBrowserExceptionModel> handleBrowser(BrowserException e) {
        if (log.isTraceEnabled()) {
            log.trace("Handling browser exception", e);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(createBrowserExceptionModel(e));
    }

    private CodedBrowserExceptionModel createBrowserExceptionModel(Throwable exception) {
        return createBrowserExceptionModel(exception, null, null);
    }

    @ExceptionHandler(CodedBrowserException.class)
    public ResponseEntity<CodedBrowserExceptionModel> handleCodedBrowser(CodedBrowserException e) {
        if (log.isTraceEnabled()) {
            log.trace("Handling coded browser exception", e);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(createBrowserExceptionModel(e));
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<SizeLimitExceptionModel> handleSizeLimit(SizeLimitExceededException e) {
        if (log.isTraceEnabled()) {
            log.trace("Handling size limit exception", e);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new SizeLimitExceptionModel("Faili ei saa üles laadida, kuna fail on liiga suur. Faili suurus võib olla kuni 10 MB."));
    }
}
