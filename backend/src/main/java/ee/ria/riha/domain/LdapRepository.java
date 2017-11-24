package ee.ria.riha.domain;

import ee.ria.riha.conf.ApplicationProperties.LdapRepositoryProperties;
import ee.ria.riha.domain.model.LdapUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.util.Assert;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class LdapRepository {

    private static final String PERSONAL_CODE_ATTRIBUTE = "uid";
    private static final String GIVEN_NAME_ATTRIBUTE = "givenName";
    private static final String SURNAME_ATTRIBUTE = "sn";
    private static final String COMMON_NAME_ATTRIBUTE = "cn";
    private static final String EMAIL_ATTRIBUTE = "mail";
    private static final String MEMBER_OF_ATTRIBUTE = "memberOf";
    private static final String APPROVERS_GROUPS_COMMON_NAME_ATTRIBUTE_VALUE = "*-hindaja";

    private static final AttributesMapper<LdapUser> LDAP_USER_ATTRIBUTES_MAPPER = attributes -> LdapUser.builder()
            .personalCode((String) attributes.get(PERSONAL_CODE_ATTRIBUTE).get())
            .givenName(attributes.get(GIVEN_NAME_ATTRIBUTE) == null
                    ? null
                    : (String) attributes.get(GIVEN_NAME_ATTRIBUTE).get())
            .surname(attributes.get(SURNAME_ATTRIBUTE) == null
                    ? null
                    : (String) attributes.get(SURNAME_ATTRIBUTE).get())
            .commonName(attributes.get(COMMON_NAME_ATTRIBUTE) == null
                    ? null
                    : (String) attributes.get(COMMON_NAME_ATTRIBUTE).get())
            .mail(attributes.get(EMAIL_ATTRIBUTE) == null
                    ? null
                    : (String) attributes.get(EMAIL_ATTRIBUTE).get())
            .build();

    private static final ContextMapper<Name> GROUP_DN_CONTEXT_MAPPER = ctx -> {
        DirContextAdapter dirContextAdapter = (DirContextAdapter) ctx;
        return dirContextAdapter.getDn();
    };

    private Name baseDn;
    private Name userSearchBase;
    private Name groupSearchBase;
    private LdapTemplate ldapTemplate;

    public LdapRepository(LdapContextSource ldapContextSource, LdapRepositoryProperties ldapRepositoryProperties) {
        Assert.notNull(ldapContextSource, "LDAP context source must not be null");
        this.ldapTemplate = new LdapTemplate(ldapContextSource);

        this.baseDn = ldapContextSource.getBaseLdapName();

        Assert.hasText(ldapRepositoryProperties.getUserSearchBase(), "userSearchBase must be provided");
        this.userSearchBase = LdapUtils.newLdapName(ldapRepositoryProperties.getUserSearchBase());

        Assert.hasText(ldapRepositoryProperties.getGroupSearchBase(), "groupSearchBase must be provided");
        this.groupSearchBase = LdapUtils.newLdapName(ldapRepositoryProperties.getGroupSearchBase());
    }

    public List<LdapUser> findLdapUsersByPersonalCodes(Set<String> personalCodes) {
        if (personalCodes.isEmpty()) {
            log.info("Nothing to look for as personal codes were not provided");
            return new ArrayList<>();
        }

        OrFilter filter = new OrFilter();
        for (String personalCode : personalCodes) {
            filter.or(new EqualsFilter(PERSONAL_CODE_ATTRIBUTE, personalCode));
        }

        return ldapTemplate.search(userSearchBase, filter.encode(), LDAP_USER_ATTRIBUTES_MAPPER);
    }

    public List<LdapUser> getAllApprovers() {
        List<Name> approversGroupsEntryDNs = getApproverGroupsDNs();

        OrFilter filter = new OrFilter();
        for (Name entryDN : approversGroupsEntryDNs) {
            LdapName fullDn = LdapNameBuilder.newInstance(baseDn).add(entryDN).build();
            filter.or(new EqualsFilter(MEMBER_OF_ATTRIBUTE, fullDn.toString()));
        }

        return ldapTemplate.search(userSearchBase, filter.encode(), LDAP_USER_ATTRIBUTES_MAPPER);
    }

    private List<Name> getApproverGroupsDNs() {
        LikeFilter filter = new LikeFilter(COMMON_NAME_ATTRIBUTE, APPROVERS_GROUPS_COMMON_NAME_ATTRIBUTE_VALUE);

        return ldapTemplate.search(groupSearchBase, filter.encode(), GROUP_DN_CONTEXT_MAPPER);
    }
}
