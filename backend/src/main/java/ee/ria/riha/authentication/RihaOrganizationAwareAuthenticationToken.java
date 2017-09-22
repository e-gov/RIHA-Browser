package ee.ria.riha.authentication;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.*;

/**
 * Authentication token that is aware of {@link RihaUserDetails} organizations and is able to select one organization as
 * active. Effective set of authorities is a combination of base and active organization authorities.
 *
 * @author Valentin Suhnjov
 */
@Slf4j
public class RihaOrganizationAwareAuthenticationToken extends PreAuthenticatedAuthenticationToken {

    private RihaOrganization activeOrganization;
    private Multimap<RihaOrganization, GrantedAuthority> organizationAuthorities = ImmutableMultimap.of();
    private Map<String, RihaOrganization> organizationsByCode = ImmutableMap.of();
    private Set<GrantedAuthority> effectiveAuthorities;

    public RihaOrganizationAwareAuthenticationToken(Object principal, Object credentials,
                                                    Collection<? extends GrantedAuthority> baseAuthorities) {
        super(principal, credentials, baseAuthorities);
        initOrganizations(principal);
        this.effectiveAuthorities = getEffectiveAuthorities();
        setAuthenticated(true);
    }

    private void initOrganizations(Object principal) {
        if (!(principal instanceof RihaUserDetails)) {
            return;
        }

        RihaUserDetails userDetails = (RihaUserDetails) principal;
        this.organizationAuthorities = ImmutableMultimap.copyOf(userDetails.getOrganizationAuthorities());

        Map<String, RihaOrganization> orgRoles = new HashMap<>();
        userDetails.getOrganizationAuthorities().keys().forEach(o -> orgRoles.put(o.getCode(), o));
        this.organizationsByCode = ImmutableMap.copyOf(orgRoles);

    }

    private Set<GrantedAuthority> getEffectiveAuthorities() {
        Set<GrantedAuthority> combinedAuthorities = new HashSet<>();

        if (super.getAuthorities() != null) {
            combinedAuthorities.addAll(super.getAuthorities());
        }

        if (this.activeOrganization != null) {
            combinedAuthorities.addAll(organizationAuthorities.get(this.activeOrganization));
        }

        return ImmutableSet.copyOf(combinedAuthorities);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.effectiveAuthorities;
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
            if (!organizationsByCode.containsKey(organizationCode)) {
                throw new IllegalArgumentException("User is not part of organization with code: " + organizationCode);
            }
            this.activeOrganization = organizationsByCode.get(organizationCode);
            if (log.isDebugEnabled()) {
                log.debug("Active organization is set to {}", activeOrganization);
            }
        }

        this.effectiveAuthorities = getEffectiveAuthorities();
    }

    public Multimap<RihaOrganization, GrantedAuthority> getOrganizationAuthorities() {
        return organizationAuthorities;
    }
}
