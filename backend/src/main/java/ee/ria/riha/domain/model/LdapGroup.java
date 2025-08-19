package ee.ria.riha.domain.model;

import javax.naming.Name;
import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

/**
 * @author Valentin Suhnjov
 */
@Data
@Entry(objectClasses = {"groupOfNames"})
public class LdapGroup {

  @Id private Name dn;

  @Attribute(name = "cn")
  private String commonName;

  @Attribute(name = "displayName")
  private String displayName;
}
