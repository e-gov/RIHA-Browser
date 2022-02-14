package ee.ria.riha.client;

/**
 * @author Valentin Suhnjov
 */
public enum OperationType {
    GET("get"),
    PUT("put"),
    POST("post"),
    DELETE("delete"),
    COUNT("count");

    private final String value;

    OperationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
