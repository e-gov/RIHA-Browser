package ee.ria.riha.authentication;

import lombok.ToString;

import java.security.Principal;

/**
 * Principal representing EstEID user.
 *
 * @author Valentin Suhnjov
 */
@ToString
public class EstEIDPrincipal implements Principal {

    private String serialNumber;
    private String givenName;
    private String surname;

    public EstEIDPrincipal(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String getName() {
        return serialNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
