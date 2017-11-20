package ee.ria.riha.domain;

import ee.ria.riha.conf.ApplicationProperties;
import ee.ria.riha.domain.model.LdapUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
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

    private final String userSearchBase;
    private LdapTemplate ldapTemplate;

    public LdapRepository(LdapContextSource ldapContextSource, ApplicationProperties applicationProperties) {
        Assert.notNull(ldapContextSource, "LDAP context source must not be null");
        ldapTemplate = new LdapTemplate(ldapContextSource);
        userSearchBase = applicationProperties.getLdapSearch().getUserSearchBase();
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
}
