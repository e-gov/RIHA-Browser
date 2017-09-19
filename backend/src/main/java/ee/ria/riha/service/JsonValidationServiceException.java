package ee.ria.riha.service;

/**
 * Runtime exception indicating that validation service caught unrecoverable exception.
 *
 * @author Valentin Suhnjov
 */
public class JsonValidationServiceException extends BrowserException {

    public JsonValidationServiceException(String message, Exception e) {
        super(message, e);
    }
}
