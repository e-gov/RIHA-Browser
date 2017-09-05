package ee.ria.riha.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collection;

/**
 * RIHA user details including additional information like personal code and name.
 *
 * @author Valentin Suhnjov
 */
public class RihaUserDetails implements UserDetails {

    private UserDetails delegate;

    private String personalCode;
    private String firstName;
    private String lastName;
    private MultiValueMap<RihaOrganization, String> organizations = new LinkedMultiValueMap<>();

    public RihaUserDetails(UserDetails delegate, String personalCode) {
        this.delegate = delegate;
        this.personalCode = personalCode;
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

    public MultiValueMap<RihaOrganization, String> getOrganizations() {
        return organizations;
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
