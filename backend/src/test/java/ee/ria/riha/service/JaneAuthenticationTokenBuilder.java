package ee.ria.riha.service;

import com.google.common.collect.ImmutableMultimap;
import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaOrganizationAwareAuthenticationToken;
import ee.ria.riha.authentication.RihaUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * Builder for Jane Doe authentication token. <p> Jane is a user with first name <strong>Jane</strong> and last name
 * <strong>Doe</strong>. Her personal code is <strong>EE40102031234</strong>. Jane user name is
 * <strong>jane.doe</strong> and password is <strong>strong</strong>. Jane base authority is
 * <strong>ROLE_RIHA_USER</strong>. Jane is a member of organization with name <strong>Acme org</strong> and code
 * <strong>555010203</strong> where she poses authority <strong>ROLE_KIRJELDAJA</strong>.</p>
 *
 * @author Valentin Suhnjov
 */
public class JaneAuthenticationTokenBuilder {

    private String userFirstName = "Jane";
    private String userLastName = "Doe";

    private String username = "jane.doe";
    private String password = "strong";
    private List<GrantedAuthority> baseAuthorities = AuthorityUtils.createAuthorityList("ROLE_RIHA_USER");

    private String personalCode = "EE40102031234";

    private ImmutableMultimap<RihaOrganization, GrantedAuthority> organizations = ImmutableMultimap.of(
            new RihaOrganization("555010203", "Acme org"),
            new SimpleGrantedAuthority("ROLE_KIRJELDAJA"));

    public static JaneAuthenticationTokenBuilder builder() {
        return new JaneAuthenticationTokenBuilder();
    }

    public RihaOrganizationAwareAuthenticationToken build() {
        RihaUserDetails userDetails = new RihaUserDetails(
                new User(username, password, baseAuthorities),
                personalCode,
                organizations);
        userDetails.setFirstName(userFirstName);
        userDetails.setLastName(userLastName);

        return new RihaOrganizationAwareAuthenticationToken(userDetails, null, baseAuthorities);
    }

    public JaneAuthenticationTokenBuilder setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
        return this;
    }

    public JaneAuthenticationTokenBuilder setUserLastName(String userLastName) {
        this.userLastName = userLastName;
        return this;
    }

    public JaneAuthenticationTokenBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public JaneAuthenticationTokenBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public JaneAuthenticationTokenBuilder setBaseAuthorities(
            List<GrantedAuthority> baseAuthorities) {
        this.baseAuthorities = baseAuthorities;
        return this;
    }

    public JaneAuthenticationTokenBuilder setPersonalCode(String personalCode) {
        this.personalCode = personalCode;
        return this;
    }

    public JaneAuthenticationTokenBuilder setOrganizations(
            ImmutableMultimap<RihaOrganization, GrantedAuthority> organizations) {
        this.organizations = organizations;
        return this;
    }
}
