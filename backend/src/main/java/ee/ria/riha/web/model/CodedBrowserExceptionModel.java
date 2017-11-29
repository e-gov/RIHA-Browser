package ee.ria.riha.web.model;

import java.util.Date;

/**
 * Model for exception that contains error code and error message with error details.
 *
 * @author Valentin Suhnjov
 */
public class CodedBrowserExceptionModel {

    private Date timestamp = new Date();
    private Class type;
    private String code;
    private String message;
    private Object[] args;

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

}
