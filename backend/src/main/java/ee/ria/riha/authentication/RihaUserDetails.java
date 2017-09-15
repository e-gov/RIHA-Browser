package ee.ria.riha.authentication;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;

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
    private Multimap<RihaOrganization, GrantedAuthority> organizationRoles = ImmutableListMultimap.of();
    private Map<String, RihaOrganization> organizations = ImmutableMap.of();
    private RihaOrganization activeOrganization = null;
    private Set<GrantedAuthority> effectiveAuthorities;

    public RihaUserDetails(UserDetails delegate, String personalCode) {
        this(delegate, personalCode, null);
    }

    public RihaUserDetails(UserDetails delegate, String personalCode,
                           Multimap<RihaOrganization, GrantedAuthority> organizationRoles) {
        Assert.notNull(delegate, "delegate should not be null");
        Assert.hasText(personalCode, "personalCode should not be empty");

        this.delegate = delegate;
        this.personalCode = personalCode;

        if (organizationRoles != null && !organizationRoles.isEmpty()) {
            this.organizationRoles = ImmutableListMultimap.copyOf(organizationRoles);

            Map<String, RihaOrganization> orgRoles = new HashMap<>();
            organizationRoles.keys().forEach(o -> orgRoles.put(o.getCode(), o));
            this.organizations = ImmutableMap.copyOf(orgRoles);
        }

        this.effectiveAuthorities = getEffectiveAuthorities();
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

    public Multimap<RihaOrganization, GrantedAuthority> getOrganizationRoles() {
        return organizationRoles;
    }

    public RihaOrganization getActiveOrganization() {
        return activeOrganization;
    }

    /**
     * Either sets or clears active organization. Active organization must be one of the users organizationRoles.
     *
     * @param organizationCode - new active organization
     */
    public void setActiveOrganization(String organizationCode) {
        if (log.isDebugEnabled()) {
            log.debug("Setting active organization to organization with code: {}", organizationCode);
        }

        if (organizationCode == null) {
            if (log.isDebugEnabled()) {
                log.debug("Clearing active organization");
            }
            this.activeOrganization = null;
        } else {
            if (!organizations.containsKey(organizationCode)) {
                throw new IllegalArgumentException("User is not part of organization with code: " + organizationCode);
            }
            this.activeOrganization = organizations.get(organizationCode);
            if (log.isDebugEnabled()) {
                log.debug("Active organization is set to {}", activeOrganization);
            }
        }

        this.effectiveAuthorities = getEffectiveAuthorities();
    }

    private Set<GrantedAuthority> getEffectiveAuthorities() {
        SortedSet<GrantedAuthority> combinedAuthorities = new TreeSet<>(new AuthorityComparator());

        combinedAuthorities.addAll(delegate.getAuthorities());

        if (activeOrganization != null) {
            combinedAuthorities.addAll(organizationRoles.get(activeOrganization));
        }

        return ImmutableSortedSet.copyOfSorted(combinedAuthorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return effectiveAuthorities;
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

    private static class AuthorityComparator implements Comparator<GrantedAuthority>,
            Serializable {
        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            if (g2.getAuthority() == null) {
                return -1;
            }

            if (g1.getAuthority() == null) {
                return 1;
            }

            return g1.getAuthority().compareTo(g2.getAuthority());
        }
    }
}
