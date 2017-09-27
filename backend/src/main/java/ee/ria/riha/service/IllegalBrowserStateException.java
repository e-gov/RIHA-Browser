package ee.ria.riha.service;

/**
 * @author Valentin Suhnjov
 */
public class IllegalBrowserStateException extends BrowserException {

    public IllegalBrowserStateException(String message) {
        super(message);
    }

    public IllegalBrowserStateException() {
        super();
    }

    public IllegalBrowserStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalBrowserStateException(Throwable cause) {
        super(cause);
    }
}