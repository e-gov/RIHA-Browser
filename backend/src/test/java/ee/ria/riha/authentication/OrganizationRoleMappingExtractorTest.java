package ee.ria.riha.authentication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.Parameter;
import org.mockito.internal.matchers.Or;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class OrganizationRoleMappingExtractorTest {

    @Parameters(name = "{index}: extract({0}), expectedOrganizationRoleMapping = {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"12345-hindaja", createExpectedOrganizationRoleMapping("12345", "ROLE_HINDAJA")},
                {"123-456-789-kirjeldaja", createExpectedOrganizationRoleMapping("123-456-789", "ROLE_KIRJELDAJA")},
                {"12345-testija", createExpectedOrganizationRoleMapping("12345", "ROLE_TESTIJA")},
                {null, null},
                {"", null},
                {"-testija", null},
                {"123-456-abc_def-", null},
                {"this-is-valid-1234567890-.&!+-#$%^-nA_Me", createExpectedOrganizationRoleMapping(
                        "this-is-valid-1234567890-.&!+-#$%^", "ROLE_NA_ME")},
                {"this-is-not-valid-common-name!", null},
                {"atLeastOneDashMustPresent", null}});
    }

    private static final String DISPLAY_NAME = "Display name is unnecessary in testing purposes";
    private OrganizationRoleMappingExtractor organizationRoleMappingExtractor = new OrganizationRoleMappingExtractor();

    @Parameter
    public String commonNameInput;
    @Parameter(1)
    public OrganizationRoleMapping expectedOrganizationRoleMapping;

    private static OrganizationRoleMapping createExpectedOrganizationRoleMapping(String expectedCode, String expectedRole) {
        OrganizationRoleMapping expectedOrganizationRoleMapping = new OrganizationRoleMapping();

        expectedOrganizationRoleMapping.setCode(expectedCode);
        expectedOrganizationRoleMapping.setAuthority(new SimpleGrantedAuthority(expectedRole));
        expectedOrganizationRoleMapping.setName(DISPLAY_NAME);

        return expectedOrganizationRoleMapping;
    }

    @Test
    public void test() {
        OrganizationRoleMapping organizationRoleMapping = organizationRoleMappingExtractor.extract(commonNameInput, DISPLAY_NAME);
        assertThat(organizationRoleMapping, equalTo(expectedOrganizationRoleMapping));
    }
}
