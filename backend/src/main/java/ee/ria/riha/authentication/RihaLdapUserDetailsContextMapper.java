package ee.ria.riha.authentication;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.util.Assert;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.ldap.LdapName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.ldap.support.LdapUtils.convertLdapException;
import static org.springframework.ldap.support.LdapUtils.newLdapName;

/**
 * @author Valentin Suhnjov
 */
@Slf4j
public class RihaLdapUserDetailsContextMapper extends LdapUserDetailsMapper {

    private static final String COMMON_NAME_TOKEN_SEPARATOR = "-";

    private static final String UID_ATTRIBUTE = "uid";
    private static final String MEMBER_OF_ATTRIBUTE = "memberof";
    private static final String COMMON_NAME_ATTRIBUTE = "cn";
    private static final String DISPLAY_NAME_ATTRIBUTE = "displayname";
    private static final String ROLE_PREFIX = "ROLE_";
    private final String DEFAULT_RIHA_USER_ROLE = "ROLE_RIHA_USER";

    private LdapTemplate ldapTemplate;

    public RihaLdapUserDetailsContextMapper(LdapContextSource ldapContextSource) {
        Assert.notNull(ldapContextSource, "LDAP context source must not be null");
        ldapTemplate = new LdapTemplate(ldapContextSource);
    }

    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
                                          Collection<? extends GrantedAuthority> authorities) {
        // User that was found in the context is a valid RIHA user, so apply default role
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(authorities);
        grantedAuthorities.add(new SimpleGrantedAuthority(DEFAULT_RIHA_USER_ROLE));

        UserDetails userDetails = super.mapUserFromContext(ctx, username, grantedAuthorities);

        return new RihaUserDetails(userDetails, ctx.getStringAttribute(UID_ATTRIBUTE),
                                   getUserOrganizationRoles(ctx));
    }

    private Multimap<RihaOrganization, GrantedAuthority> getUserOrganizationRoles(DirContextOperations ctx) {
        Multimap<RihaOrganization, GrantedAuthority> organizationRoles = ArrayListMultimap.create();

        String[] groupDns = ctx.getStringAttributes(MEMBER_OF_ATTRIBUTE);
        if (groupDns != null) {
            for (String groupDn : groupDns) {
                DirContextOperations groupCtx = lookupGroup(groupDn);
                if (groupCtx != null) {
                    OrganizationRoleMapping organizationRoleMapping = getOrganizationRoleMapping(groupCtx);

                    if (organizationRoleMapping != null) {
                        RihaOrganization rihaOrganization = new RihaOrganization(organizationRoleMapping.getCode(),
                                                                                 organizationRoleMapping.getName());
                        organizationRoles.put(rihaOrganization, organizationRoleMapping.getAuthority());
                    }
                }
            }
        }

        return organizationRoles;
    }

    private DirContextOperations lookupGroup(String groupDnStr) {
        LdapName groupDn = normalizeGroupDn(groupDnStr);

        try {
            return ldapTemplate.lookupContext(groupDn);
        } catch (org.springframework.ldap.NamingException e) {
            log.warn("Lookup for user group '" + groupDn + "' has failed");
            return null;
        }
    }

    private LdapName normalizeGroupDn(String groupDnStr) {
        LdapName groupDn = newLdapName(groupDnStr);

        Name baseDn = getBaseDn();
        if (groupDn.startsWith(baseDn)) {
            return LdapUtils.removeFirst(groupDn, baseDn);
        }

        return groupDn;
    }

    private OrganizationRoleMapping getOrganizationRoleMapping(DirContextOperations groupCtx) {
        OrganizationRoleMapping organizationRoleMapping = new OrganizationRoleMapping();

        String commonName = groupCtx.getStringAttribute(COMMON_NAME_ATTRIBUTE);
        if (commonName == null) {
            log.debug("Could not find common name of organization '{}'", groupCtx.getDn());
            return null;
        }

        String[] cnTokens = commonName.split(COMMON_NAME_TOKEN_SEPARATOR);
        if (cnTokens.length != 2) {
            log.debug("Expecting two tokens in organization common name '{}' but found {}", commonName,
                      cnTokens.length);
        }

        organizationRoleMapping.setCode(cnTokens[0]);
        organizationRoleMapping.setAuthority(new SimpleGrantedAuthority(ROLE_PREFIX + cnTokens[1].toUpperCase()));
        organizationRoleMapping.setName(groupCtx.getStringAttribute(DISPLAY_NAME_ATTRIBUTE));

        return organizationRoleMapping;
    }

    private Name getBaseDn() {
        try {
            return newLdapName(ldapTemplate.getContextSource().getReadOnlyContext().getNameInNamespace());
        } catch (NamingException e) {
            throw convertLdapException(e);
        }
    }

    @Data
    private class OrganizationRoleMapping {
        private String name;
        private String code;
        private GrantedAuthority authority;
    }
}
