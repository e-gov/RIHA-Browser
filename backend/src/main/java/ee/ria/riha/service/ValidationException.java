package ee.ria.riha.service;

/**
 * @author Valentin Suhnjov
 */
public class ValidationException extends BrowserException {

    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
        super(message);
    }

}
