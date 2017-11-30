package ee.ria.riha.service;

/**
 * Indicates that external entity is not found
 *
 * @author Valentin Suhnjov
 */
public class ObjectNotFoundException extends CodedBrowserException {

    public ObjectNotFoundException(String code) {
        super(code);
    }

    public ObjectNotFoundException(String code, Throwable e) {
        super(code, e);
    }

    public ObjectNotFoundException(String code, Object... args) {
        super(code, args);
    }

    public ObjectNotFoundException(String code, Throwable e, Object... args) {
        super(code, e, args);
    }

}
