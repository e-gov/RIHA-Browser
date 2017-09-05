package ee.ria.riha.authentication;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Valentin Suhnjov
 */
public class RihaLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData,
                                                                        String username) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

}