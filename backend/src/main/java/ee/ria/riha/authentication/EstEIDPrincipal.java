package ee.ria.riha.authentication;

import java.security.Principal;

/**
 * Principal representing EstEID user.
 *
 * @author Valentin Suhnjov
 */
public class EstEIDPrincipal implements Principal {

    private String personalCode;

    public EstEIDPrincipal(String personalCode) {
        this.personalCode = personalCode;
    }

    @Override
    public String getName() {
        return personalCode;
    }

    public String getPersonalCode() {
        return personalCode;
    }
}
