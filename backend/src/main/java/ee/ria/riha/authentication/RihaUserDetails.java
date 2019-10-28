package ee.ria.riha.authentication;

import com.google.common.collect.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.Assert;

import java.util.*;

import static org.springframework.util.StringUtils.hasText;

/**
 * RIHA user details including additional information like personal code and name.
 *
 * @author Valentin Suhnjov
 */
@Slf4j
@Getter
public class RihaUserDetails implements UserDetails, OidcUser {

    private UserDetails delegate;

    private ImmutableMap<String, RihaOrganization> organizationsByCode;
    private RihaOrganization activeOrganization;

    private String personalCode;
    private String firstName;
    private String lastName;
    private ImmutableMultimap<RihaOrganization, GrantedAuthority> organizationAuthorities;
    private transient OidcUserRequest userRequest;

    public RihaUserDetails(UserDetails delegate, String personalCode) {
        this(delegate, personalCode, null);
    }

    public RihaUserDetails(UserDetails delegate, String personalCode,
                           Multimap<RihaOrganization, GrantedAuthority> organizationAuthorities) {
        Assert.notNull(delegate, "delegate should not be null");
        Assert.hasText(personalCode, "personalCode should not be empty");

        this.delegate = delegate;
        this.personalCode = personalCode;

        initOrganizationAuthorities(organizationAuthorities);
    }

    private void initOrganizationAuthorities(Multimap<RihaOrganization, GrantedAuthority> organizationAuthorities) {
        this.organizationAuthorities = organizationAuthorities != null
                ? ImmutableListMultimap.copyOf(organizationAuthorities)
                : ImmutableMultimap.of();


        Map<String, RihaOrganization> orgRoles = new HashMap<>();
        getOrganizationAuthorities().keys().forEach(o -> orgRoles.put(o.getCode(), o));
        this.organizationsByCode = ImmutableMap.copyOf(orgRoles);
    }

    public String getTaraAmr() {

        if (getUserRequest() == null
                || getUserRequest().getIdToken() == null
                || getUserRequest().getIdToken().getClaims() == null
                || getUserRequest().getIdToken().getClaims().get("amr") == null) {

            return null;
        }

        return getUserRequest().getIdToken().getClaims().get("amr").toString();

    }

    public String getFullName() {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (hasText(firstName)) {
            stringJoiner.add(firstName);
        }

        if (hasText(lastName)) {
            stringJoiner.add(lastName);
        }

        return stringJoiner.toString();
    }

    public Multimap<RihaOrganization, GrantedAuthority> getOrganizationAuthorities() {
        return organizationAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getEffectiveAuthorities();
    }

    @Override
    public Map<String, Object> getAttributes() {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("personalCode", personalCode);
        attributes.put("firstName", firstName);
        attributes.put("lastName", lastName);
        return attributes;
    }

    private Set<GrantedAuthority> getEffectiveAuthorities() {
        Set<GrantedAuthority> combinedAuthorities = new HashSet<>();

        if (delegate.getAuthorities() != null) {
            combinedAuthorities.addAll(delegate.getAuthorities());
        }

        if (this.activeOrganization != null) {
            combinedAuthorities.addAll(organizationAuthorities.get(this.activeOrganization));
        }

        return ImmutableSet.copyOf(combinedAuthorities);
    }

    @Override
    public String getPassword() {
        return delegate.getPassword();
    }

    @Override
    public String getUsername() {
        return delegate.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return delegate.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return delegate.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return delegate.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return delegate.isEnabled();
    }

    @Override
    public String getName() {
        return getFullName();
    }

    @Override
    public Map<String, Object> getClaims() {
        if (userRequest == null || userRequest.getIdToken() == null) {
            return null;
        }
        return userRequest.getIdToken().getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {

        if (userRequest == null) {
            return null;
        }

        return new OidcUserInfo(userRequest.getIdToken().getClaims());
    }

    @Override
    public OidcIdToken getIdToken() {
        return userRequest != null
                ? userRequest.getIdToken()
                : null;
    }

    public void setUserRequest(OidcUserRequest userRequest) {
        this.userRequest = userRequest;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setActiveOrganization(RihaOrganization activeOrganization) {
        this.activeOrganization = activeOrganization;
    }

    public void setActiveOrganization(String organisationCode) {
        if (organisationCode != null && this.getOrganizationsByCode() != null && getOrganizationsByCode().containsKey(organisationCode)) {
            setActiveOrganization(getOrganizationsByCode().get(organisationCode));
        } else if (organisationCode == null) {
            this.setActiveOrganization((RihaOrganization) null);
        }
    }




}
