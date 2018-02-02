package ee.ria.riha.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class RihaLdapUserDetailsContextMapperHelper {

    private static final String COMMON_NAME_TOKEN_SEPARATOR = "-";
    private static final String COMMON_NAME_ATTRIBUTE = "cn";
    private static final String DISPLAY_NAME_ATTRIBUTE = "displayname";
    private static final Pattern COMMON_NAME_CONTAINS_ROLE_INFO_PATTERN = Pattern.compile("^.+-[a-zA-Z_]+$");

    public OrganizationRoleMapping getOrganizationRoleMapping(DirContextOperations groupCtx) {
        OrganizationRoleMapping organizationRoleMapping = new OrganizationRoleMapping();

        String commonName = groupCtx.getStringAttribute(COMMON_NAME_ATTRIBUTE);
        if (commonName == null) {
            log.debug("Could not find common name of organization '{}'", groupCtx.getDn());
            return null;
        }

        Matcher roleMatcher = COMMON_NAME_CONTAINS_ROLE_INFO_PATTERN.matcher(commonName);
        if (!roleMatcher.matches()) {
            log.debug("Organization common name '{}' does not contain role information", commonName);
            return null;
        }

        String[] cnTokens = getCommonNameTokens(commonName);

        organizationRoleMapping.setCode(cnTokens[0]);
        organizationRoleMapping.setAuthority(new SimpleGrantedAuthority(RihaLdapUserDetailsContextMapper.ROLE_PREFIX + cnTokens[1].toUpperCase()));
        organizationRoleMapping.setName(groupCtx.getStringAttribute(DISPLAY_NAME_ATTRIBUTE));

        return organizationRoleMapping;
    }

    private String[] getCommonNameTokens(String commonName) {
        return new String[] {getCodeToken(commonName), getRoleToken(commonName)};
    }

    private int getCodeAndRoleSeparatorIndex(String commonName) {
        return commonName.lastIndexOf(COMMON_NAME_TOKEN_SEPARATOR);
    }

    private String getCodeToken(String commonName) {
        int codeAndRoleSeparatorIndex = getCodeAndRoleSeparatorIndex(commonName);
        return commonName.substring(0, codeAndRoleSeparatorIndex);
    }

    private String getRoleToken(String commonName) {
        int codeAndRoleSeparatorIndex = getCodeAndRoleSeparatorIndex(commonName);
        return commonName.substring(codeAndRoleSeparatorIndex + 1, commonName.length());
    }
}
