package ee.ria.riha.service;

/**
 * @author Valentin Suhnjov
 */
public class ObjectNotFoundException extends BrowserException {

    public ObjectNotFoundException() {
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectNotFoundException(Throwable cause) {
        super(cause);
    }

}
