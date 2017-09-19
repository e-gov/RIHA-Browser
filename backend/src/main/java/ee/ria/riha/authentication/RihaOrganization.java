package ee.ria.riha.authentication;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

/**
 * Immutable representation of organization that user belongs to.
 *
 * @author Valentin Suhnjov
 */
@EqualsAndHashCode(of = "code")
@Getter
@ToString
public class RihaOrganization {

    private String code;
    private String name;

    public RihaOrganization(String code, String name) {
        Assert.hasText(code, "code must not be null");

        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
