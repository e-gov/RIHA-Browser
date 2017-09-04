package ee.ria.riha.authentication;

import ee.ria.riha.model.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.security.core.GrantedAuthority;
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

    private LdapTemplate ldapTemplate;

    public RihaLdapUserDetailsContextMapper(LdapContextSource ldapContextSource) {
        Assert.notNull(ldapContextSource, "LDAP context source must not be null");

        ldapTemplate = new LdapTemplate(ldapContextSource);
    }

    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username,
                                          Collection<? extends GrantedAuthority> authorities) {
        UserDetails userDetails = super.mapUserFromContext(ctx, username, authorities);

        RihaUserDetails rihaUserDetails = new RihaUserDetails(userDetails, ctx.getStringAttribute(UID_ATTRIBUTE));
        rihaUserDetails.getOrganizations().addAll(getUserOrganizations(ctx));

        return rihaUserDetails;
    }

    private List<Organization> getUserOrganizations(DirContextOperations ctx) {
        List<Organization> organizations = new ArrayList<>();

        String[] groupDns = ctx.getStringAttributes(MEMBER_OF_ATTRIBUTE);
        if (groupDns != null) {
            for (String groupDn : groupDns) {
                DirContextOperations groupCtx = lookupGroup(groupDn);
                if (groupCtx != null) {
                    organizations.add(getOrganization(groupCtx));
                }
            }
        }

        return organizations;
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

    private Organization getOrganization(DirContextOperations groupCtx) {
        Organization organization = new Organization();

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

        organization.setCode(cnTokens[0]);
        organization.setName(groupCtx.getStringAttribute(DISPLAY_NAME_ATTRIBUTE));

        return organization;
    }

    private Name getBaseDn() {
        try {
            return newLdapName(ldapTemplate.getContextSource().getReadOnlyContext().getNameInNamespace());
        } catch (NamingException e) {
            throw convertLdapException(e);
        }
    }
}
