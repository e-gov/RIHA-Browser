package ee.ria.riha.domain;

import ee.ria.riha.conf.ApplicationProperties.LdapRepositoryProperties;
import ee.ria.riha.domain.model.LdapGroup;
import ee.ria.riha.domain.model.LdapUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AbstractFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.util.Assert;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class LdapRepository {

    private static final String USER_ID_ATTRIBUTE = "uid";
    private static final String COMMON_NAME_ATTRIBUTE = "cn";
    private static final String MEMBER_OF_ATTRIBUTE = "memberOf";
    private static final String APPROVER_GROUP_COMMON_NAME_PATTERN = "*-hindaja";
    private static final String ALL_NON_OPERATIONAL_ATTRIBUTES = "*";

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
            log.debug("Will not search for users since list of personal codes is empty");
            return new ArrayList<>();
        }

        OrFilter filter = new OrFilter();
        personalCodes.stream()
                .map(personalCode -> new EqualsFilter(USER_ID_ATTRIBUTE, personalCode))
                .forEach(filter::or);

        return findLdapUsers(filter);
    }

    private List<LdapUser> findLdapUsers(AbstractFilter filter) {
        LdapQuery query = LdapQueryBuilder.query()
                .base(userSearchBase)
                .attributes(ALL_NON_OPERATIONAL_ATTRIBUTES, MEMBER_OF_ATTRIBUTE)
                .filter(filter);

        return ldapTemplate.find(query, LdapUser.class);
    }

    public List<LdapUser> getAllApprovers() {
        List<LdapGroup> approverGroups = findLdapGroups(
                new LikeFilter(COMMON_NAME_ATTRIBUTE, APPROVER_GROUP_COMMON_NAME_PATTERN));

        if (approverGroups.isEmpty()) {
            log.debug("Would not search for approvers since no approver groups found");
            return new ArrayList<>();
        }

        OrFilter filter = new OrFilter();
        approverGroups.stream()
                .map(group -> LdapNameBuilder.newInstance(baseDn)
                        .add(group.getDn())
                        .build()
                        .toString())
                .map(groupDn -> new EqualsFilter(MEMBER_OF_ATTRIBUTE, groupDn))
                .forEach(filter::or);

        return findLdapUsers(filter);
    }

    private List<LdapGroup> findLdapGroups(AbstractFilter filter) {
        LdapQuery query = LdapQueryBuilder.query()
                .base(groupSearchBase)
                .filter(filter);

        return ldapTemplate.find(query, LdapGroup.class);
    }
}
