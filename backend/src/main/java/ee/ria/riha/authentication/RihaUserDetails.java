package ee.ria.riha.authentication;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * RIHA user details including additional information like personal code and name.
 *
 * @author Valentin Suhnjov
 */
@Slf4j
public class RihaUserDetails implements UserDetails {

    private UserDetails delegate;

    private String personalCode;
    private String firstName;
    private String lastName;
    private Multimap<RihaOrganization, GrantedAuthority> organizationAuthorities;

    public RihaUserDetails(UserDetails delegate, String personalCode) {
        this(delegate, personalCode, null);
    }

    public RihaUserDetails(UserDetails delegate, String personalCode,
                           Multimap<RihaOrganization, GrantedAuthority> organizationAuthorities) {
        Assert.notNull(delegate, "delegate should not be null");
        Assert.hasText(personalCode, "personalCode should not be empty");

        this.delegate = delegate;
        this.personalCode = personalCode;
        this.organizationAuthorities = organizationAuthorities != null
                ? ImmutableListMultimap.copyOf(organizationAuthorities)
                : ImmutableMultimap.of();
    }

    public String getPersonalCode() {
        return personalCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Multimap<RihaOrganization, GrantedAuthority> getOrganizationAuthorities() {
        return organizationAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
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

}
