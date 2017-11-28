package ee.ria.riha.domain.model;

import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import java.util.List;

/**
 * Holds basic user data from LDAP
 */

@Data
@Entry(objectClasses = "person")
public class LdapUser {

    @Id
    private Name dn;

    @Attribute(name = "uid")
    private String personalCode;

    @Attribute(name = "givenName")
    private String givenName;

    @Attribute(name = "sn")
    private String surname;

    @Attribute(name = "cn")
    private String commonName;

    @Attribute(name = "mail")
    private String mail;

    @Attribute(name = "memberOf")
    private List<Name> memberOf;

}
