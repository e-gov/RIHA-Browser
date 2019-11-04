package ee.ria.riha.authentication;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Immutable representation of organization that user belongs to.
 *
 * @author Valentin Suhnjov
 */
@EqualsAndHashCode(of = "code")
@Getter
@ToString
public class RihaOrganization implements Serializable {
    private static final long serialVersionUID = 0;

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
