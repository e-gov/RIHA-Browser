package ee.ria.riha.authentication;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
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

    private static final String UID_ATTRIBUTE = "uid";
    private static final String COMMON_NAME_ATTRIBUTE = "cn";
    private static final String DISPLAY_NAME_ATTRIBUTE = "displayname";
    private static final String MEMBER_OF_ATTRIBUTE = "memberof";
    public static final String DEFAULT_RIHA_USER_ROLE = "ROLE_RIHA_USER";

    private LdapTemplate ldapTemplate;
    private OrganizationRoleMappingExtractor organizationRoleMappingExtractor = new OrganizationRoleMappingExtractor();

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
                    String commonName = groupCtx.getStringAttribute(COMMON_NAME_ATTRIBUTE);
                    String displayName = groupCtx.getStringAttribute(DISPLAY_NAME_ATTRIBUTE);

                    OrganizationRoleMapping organizationRoleMapping = organizationRoleMappingExtractor.extract(commonName, displayName);
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

    private Name getBaseDn() {
        try {
            return newLdapName(ldapTemplate.getContextSource().getReadOnlyContext().getNameInNamespace());
        } catch (NamingException e) {
            throw convertLdapException(e);
        }
    }
}
