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

import static ee.ria.riha.service.auth.RoleType.AUTHENTICATED_USER;
import static ee.ria.riha.service.auth.RoleType.PRODUCER;

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

    public static final String FIRST_NAME = "Jane";
    public static final String LAST_NAME = "Doe";
    public static final String USERNAME = "jane.doe";
    public static final String PASSWORD = "strong";
    public static final String PERSONAL_CODE = "EE40102031234";
    public static final String ORGANIZATION_CODE = "555010203";
    public static final String ORGANIZATION_NAME = "Acme org";

    private String userFirstName = FIRST_NAME;
    private String userLastName = LAST_NAME;

    private String username = USERNAME;
    private String password = PASSWORD;
    private List<GrantedAuthority> baseAuthorities = AuthorityUtils.createAuthorityList(AUTHENTICATED_USER.getRole());

    private String personalCode = PERSONAL_CODE;

    private ImmutableMultimap<RihaOrganization, GrantedAuthority> organizations = ImmutableMultimap.of(
            new RihaOrganization(ORGANIZATION_CODE, ORGANIZATION_NAME),
            new SimpleGrantedAuthority(PRODUCER.getRole()));

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
