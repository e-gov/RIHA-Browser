package ee.ria.riha.authentication;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Valentin Suhnjov
 */
@EqualsAndHashCode(of = "code")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RihaOrganization {

    private String code;
    private String name;

}
