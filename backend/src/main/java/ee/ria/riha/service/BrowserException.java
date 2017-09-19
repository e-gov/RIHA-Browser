package ee.ria.riha.service;

/**
 * @author Valentin Suhnjov
 */
public abstract class BrowserException extends RuntimeException {

    public BrowserException() {
        super();
    }

    public BrowserException(String message) {
        super(message);
    }

    public BrowserException(String message, Throwable cause) {
        super(message, cause);
    }

    public BrowserException(Throwable cause) {
        super(cause);
    }

}
