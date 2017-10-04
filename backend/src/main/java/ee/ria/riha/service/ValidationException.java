package ee.ria.riha.service;

/**
 * @author Valentin Suhnjov
 */
public class ValidationException extends BrowserException {

    private final String code;
    private final Object[] args;

    public ValidationException(String code) {
        super(code);
        this.code = code;
        this.args = new Object[0];
    }

    public ValidationException(String code, Object... args) {
        super(code);
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
