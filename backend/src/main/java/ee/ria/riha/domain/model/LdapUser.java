package ee.ria.riha.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Holds basic user data from LDAP
 */
@Getter
@Setter
@Builder
public class LdapUser {

    private String personalCode;
    private String givenName;
    private String surname;
    private String commonName;
    private String mail;
}
