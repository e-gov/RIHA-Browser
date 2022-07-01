package ee.ria.riha.web.model;

public class SizeLimitExceptionModel {
    private String message;

    public SizeLimitExceptionModel(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
