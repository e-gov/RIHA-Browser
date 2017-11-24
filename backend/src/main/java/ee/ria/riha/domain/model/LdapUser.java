package ee.ria.riha.domain.model;

import lombok.Builder;
import lombok.Data;

/**
 * Holds basic user data from LDAP
 */

@Data
@Builder
public class LdapUser {

    private String personalCode;
    private String givenName;
    private String surname;
    private String commonName;
    private String mail;

}
