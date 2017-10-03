package ee.ria.riha.web.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * @author Valentin Suhnjov
 */
@Data
public class ValidationErrorModel {

    @Setter(AccessLevel.PRIVATE)
    private String type = "validation";
    private String code;
    private String message;
    private Object[] args;

}
