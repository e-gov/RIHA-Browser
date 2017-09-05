package ee.ria.riha.authentication;

import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;

/**
 * Ldap user search that searches for necessary attributes for RIHA LDAP authorization.
 *
 * @author Valentin Suhnjov
 */
public class RihaFilterBasedLdapUserSearch extends FilterBasedLdapUserSearch {

    private static final String ALL_NON_OPERATIONAL_ATTRIBUTES = "*";
    private static final String MEMBER_OF_ATTRIBUTE = "memberOf";

    public RihaFilterBasedLdapUserSearch(String searchBase, String searchFilter,
                                         BaseLdapPathContextSource contextSource) {
        super(searchBase, searchFilter, contextSource);

        setReturningAttributes(new String[]{ALL_NON_OPERATIONAL_ATTRIBUTES, MEMBER_OF_ATTRIBUTE});
        setSearchSubtree(true);
    }
}
