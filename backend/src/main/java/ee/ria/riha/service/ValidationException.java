package ee.ria.riha.service;

/**
 * @author Valentin Suhnjov
 */
public class ValidationException extends CodedBrowserException {

    public ValidationException(String code) {
        super(code);
    }

    public ValidationException(String code, Throwable e) {
        super(code, e);
    }

    public ValidationException(String code, Object... args) {
        super(code, args);
    }

    public ValidationException(String code, Throwable e, Object... args) {
        super(code, e, args);
    }

}
