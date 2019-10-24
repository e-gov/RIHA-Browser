package ee.ria.riha.service;

/**
 * Base class for exceptions that contain error code and arguments. These exceptions, when presented to the caller,
 * allow custom handling according to the code, type and arguments.
 *
 * @author Valentin Suhnjov
 */
public class CodedBrowserException extends BrowserException {

    private final String code;
    private final transient Object[] args;

    public CodedBrowserException(String code) {
        super(code);
        this.code = code;
        this.args = new Object[0];
    }

    public CodedBrowserException(String code, Throwable e) {
        super(code, e);
        this.code = code;
        this.args = new Object[0];
    }

    public CodedBrowserException(String code, Object... args) {
        super(code);
        this.code = code;
        this.args = args;
    }

    public CodedBrowserException(String code, Throwable e, Object... args) {
        super(code, e);
        this.code = code;
        this.args = args;
    }

    public String getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }

}
