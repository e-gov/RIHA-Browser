package ee.ria.riha.service.storage;

import lombok.Getter;

/**
 * @author Valentin Suhnjov
 */
@Getter
public enum OperationType {
    GET("get"),
    COUNT("count");

    private final String uriValue;

    OperationType(String uriValue) {
        this.uriValue = uriValue;
    }
}
