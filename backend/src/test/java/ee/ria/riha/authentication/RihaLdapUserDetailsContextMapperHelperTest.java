package ee.ria.riha.authentication;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ldap.core.DirContextAdapter;

import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

public class RihaLdapUserDetailsContextMapperHelperTest {

    private static final String COMMON_NAME_ATTRIBUTE_NAME = "cn";
    private final Attribute COMMON_NAME_ATTRIBUTE = new BasicAttribute(COMMON_NAME_ATTRIBUTE_NAME);
    private DirContextAdapter dirContextAdapter = new DirContextAdapter();
    private RihaLdapUserDetailsContextMapperHelper rihaLdapUserDetailsContextMapperHelper =
            new RihaLdapUserDetailsContextMapperHelper();

    @Before
    public void setUp() {
        dirContextAdapter.setAttribute(COMMON_NAME_ATTRIBUTE);
    }

    @Test
    public void successfullyExtractsRoleWhenOneDashIsPresent() {
        dirContextAdapter.setAttributeValue(COMMON_NAME_ATTRIBUTE_NAME, "12345-hindaja");
        OrganizationRoleMapping organizationRoleMapping = rihaLdapUserDetailsContextMapperHelper.getOrganizationRoleMapping(dirContextAdapter);
        assertThat(organizationRoleMapping.getCode(), is(equalTo("12345")));
        assertThat(organizationRoleMapping.getAuthority().getAuthority(), is(equalTo("ROLE_HINDAJA")));
    }

    @Test
    public void successfullyExtractsRoleWhenSeveralDashesArePresent() {
        dirContextAdapter.setAttributeValue(COMMON_NAME_ATTRIBUTE_NAME, "123-456-789-kirjeldaja");
        OrganizationRoleMapping organizationRoleMapping = rihaLdapUserDetailsContextMapperHelper.getOrganizationRoleMapping(dirContextAdapter);
        assertThat(organizationRoleMapping.getCode(), is(equalTo("123-456-789")));
        assertThat(organizationRoleMapping.getAuthority().getAuthority(), is(equalTo("ROLE_KIRJELDAJA")));
    }

    @Test
    public void successfullyExtractsNonExistingRole() {
        dirContextAdapter.setAttributeValue(COMMON_NAME_ATTRIBUTE_NAME, "12345-testija");
        OrganizationRoleMapping organizationRoleMapping = rihaLdapUserDetailsContextMapperHelper.getOrganizationRoleMapping(dirContextAdapter);
        assertThat(organizationRoleMapping.getCode(), is(equalTo("12345")));
        assertThat(organizationRoleMapping.getAuthority().getAuthority(), is(equalTo("ROLE_TESTIJA")));
    }

    @Test
    public void doesNotExtractRoleIfCommonNameAttributeIsMissing() {
        dirContextAdapter = new DirContextAdapter();
        OrganizationRoleMapping organizationRoleMapping = rihaLdapUserDetailsContextMapperHelper.getOrganizationRoleMapping(dirContextAdapter);
        assertThat(organizationRoleMapping, is(nullValue()));
    }

    @Test
    public void doesNotExtractRoleIfCommonNameAttributeHasNoValue() {
        OrganizationRoleMapping organizationRoleMapping = rihaLdapUserDetailsContextMapperHelper.getOrganizationRoleMapping(dirContextAdapter);
        assertThat(organizationRoleMapping, is(nullValue()));
    }

    @Test
    public void doesNotExtractRoleWhenCodeTokenIsMissing() {
        dirContextAdapter.setAttributeValue(COMMON_NAME_ATTRIBUTE_NAME, "-testija");
        OrganizationRoleMapping organizationRoleMapping = rihaLdapUserDetailsContextMapperHelper.getOrganizationRoleMapping(dirContextAdapter);
        assertThat(organizationRoleMapping, is(nullValue()));
    }

    @Test
    public void doesNotExtractRoleWhenRoleTokenIsMissing() {
        dirContextAdapter.setAttributeValue(COMMON_NAME_ATTRIBUTE_NAME, "123-456-abc_def-");
        OrganizationRoleMapping organizationRoleMapping = rihaLdapUserDetailsContextMapperHelper.getOrganizationRoleMapping(dirContextAdapter);
        assertThat(organizationRoleMapping, is(nullValue()));
    }

    @Test
    public void successfullyExtractsRoleWhenCommonNameContainsLegalCharactersOnly() {
        dirContextAdapter.setAttributeValue(COMMON_NAME_ATTRIBUTE_NAME, "this-is-valid-1234567890-.&!+-#$%^-nA_Me");
        OrganizationRoleMapping organizationRoleMapping = rihaLdapUserDetailsContextMapperHelper.getOrganizationRoleMapping(dirContextAdapter);
        assertThat(organizationRoleMapping.getCode(), is(equalTo("this-is-valid-1234567890-.&!+-#$%^")));
        assertThat(organizationRoleMapping.getAuthority().getAuthority(), is(equalTo("ROLE_NA_ME")));
    }

    @Test
    public void doesNotExtractRoleWhenCommonNameContainsIllegalCharacters() {
        dirContextAdapter.setAttributeValue(COMMON_NAME_ATTRIBUTE_NAME, "this-is-not-valid-common-name!");
        OrganizationRoleMapping organizationRoleMapping = rihaLdapUserDetailsContextMapperHelper.getOrganizationRoleMapping(dirContextAdapter);
        assertThat(organizationRoleMapping, is(nullValue()));
    }

    @Test
    public void doesNotExtractRoleWhenDashIsMissing() {
        dirContextAdapter.setAttributeValue(COMMON_NAME_ATTRIBUTE_NAME, "atLeastOneDashMustPresent");
        OrganizationRoleMapping organizationRoleMapping = rihaLdapUserDetailsContextMapperHelper.getOrganizationRoleMapping(dirContextAdapter);
        assertThat(organizationRoleMapping, is(nullValue()));
    }
}
