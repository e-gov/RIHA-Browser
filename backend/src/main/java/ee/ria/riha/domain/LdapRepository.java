package ee.ria.riha.domain;

import ee.ria.riha.conf.ApplicationProperties;
import ee.ria.riha.domain.model.LdapUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.util.Assert;

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
    private static final String ASSESSORS_GROUPS_COMMON_NAME_ATTRIBUTE_VALUE = "*-hindaja";

    public static final AttributesMapper<LdapUser> LDAP_USER_ATTRIBUTES_MAPPER = attributes -> LdapUser.builder()
            .personalCode((String) attributes.get(PERSONAL_CODE_ATTRIBUTE).get())
            .givenName(attributes.get(GIVEN_NAME_ATTRIBUTE) == null ?
                    null : (String) attributes.get(GIVEN_NAME_ATTRIBUTE).get())
            .surname(attributes.get(SURNAME_ATTRIBUTE) == null ?
                    null : (String) attributes.get(SURNAME_ATTRIBUTE).get())
            .commonName(attributes.get(COMMON_NAME_ATTRIBUTE) == null ?
                    null : (String) attributes.get(COMMON_NAME_ATTRIBUTE).get())
            .mail(attributes.get(EMAIL_ATTRIBUTE) == null ?
                    null : (String) attributes.get(EMAIL_ATTRIBUTE).get())
            .build();

    private static final AttributesMapper<String> GROUP_ENTRY_DN_ATTRIBUTE_MAPPER = attributes ->
            String.format("cn=%s,ou=Groups,dc=riha,dc=ria,dc=ee", attributes.get(COMMON_NAME_ATTRIBUTE).get());

    private final String userSearchBase;
    private LdapTemplate ldapTemplate;
    private final String groupSearchBase;

    public LdapRepository(LdapContextSource ldapContextSource, ApplicationProperties applicationProperties) {
        Assert.notNull(ldapContextSource, "LDAP context source must not be null");
        ldapTemplate = new LdapTemplate(ldapContextSource);
        userSearchBase = applicationProperties.getLdapSearch().getUserSearchBase();
        groupSearchBase = applicationProperties.getLdapSearch().getGroupSearchBase();
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

    public List<LdapUser> getAllAssessors() {
        List<String> assessorsGroupsEntryDNs = getAssessorsGroupsEntryDNs();
        OrFilter filter = new OrFilter();

        for (String entryDN : assessorsGroupsEntryDNs) {
            filter.or(new EqualsFilter(MEMBER_OF_ATTRIBUTE, entryDN));
        }

        return ldapTemplate.search(userSearchBase, filter.encode(), LDAP_USER_ATTRIBUTES_MAPPER);
    }

    private List<String> getAssessorsGroupsEntryDNs() {
        LikeFilter filter = new LikeFilter(COMMON_NAME_ATTRIBUTE, ASSESSORS_GROUPS_COMMON_NAME_ATTRIBUTE_VALUE);
        return ldapTemplate.search(groupSearchBase, filter.encode(), GROUP_ENTRY_DN_ATTRIBUTE_MAPPER);
    }
}
