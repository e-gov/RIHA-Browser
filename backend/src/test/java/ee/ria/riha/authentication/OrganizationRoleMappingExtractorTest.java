package ee.ria.riha.authentication;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

public class OrganizationRoleMappingExtractorTest {

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
    public String commonNameInput;
    public OrganizationRoleMapping expectedOrganizationRoleMapping;

    private static OrganizationRoleMapping createExpectedOrganizationRoleMapping(String expectedCode, String expectedRole) {
        OrganizationRoleMapping expectedOrganizationRoleMapping = new OrganizationRoleMapping();

        expectedOrganizationRoleMapping.setCode(expectedCode);
        expectedOrganizationRoleMapping.setAuthority(new SimpleGrantedAuthority(expectedRole));
        expectedOrganizationRoleMapping.setName(DISPLAY_NAME);

        return expectedOrganizationRoleMapping;
    }

    @MethodSource("data")
    @ParameterizedTest(name = "{index}: extract({0}), expectedOrganizationRoleMapping = {1}")
    public void test(String commonNameInput, OrganizationRoleMapping expectedOrganizationRoleMapping) {
        initOrganizationRoleMappingExtractorTest(commonNameInput, expectedOrganizationRoleMapping);
        OrganizationRoleMapping organizationRoleMapping = organizationRoleMappingExtractor.extract(commonNameInput, DISPLAY_NAME);
        assertThat(organizationRoleMapping, equalTo(expectedOrganizationRoleMapping));
    }

    public void initOrganizationRoleMappingExtractorTest(String commonNameInput, OrganizationRoleMapping expectedOrganizationRoleMapping) {
        this.commonNameInput = commonNameInput;
        this.expectedOrganizationRoleMapping = expectedOrganizationRoleMapping;
    }
}
