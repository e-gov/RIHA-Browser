package ee.ria.riha.service;

/**
 * @author Valentin Suhnjov
 */
public class ValidationException extends BrowserException {

    private String code;
    private Object[] args;

    public ValidationException(String code) {
        super(code);
        this.code = code;
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
