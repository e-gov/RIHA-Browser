package ee.ria.riha.authentication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class OrganizationRoleMappingExtractorTest {

    @Parameters(name = "{index}: extract({0}), expectedCode = {1} and expectedRole = {2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"12345-hindaja", "12345", "ROLE_HINDAJA"},
                {"123-456-789-kirjeldaja", "123-456-789", "ROLE_KIRJELDAJA"},
                {"12345-testija", "12345", "ROLE_TESTIJA"},
                {null, null, null},
                {"", null, null},
                {"-testija", null, null},
                {"123-456-abc_def-", null, null},
                {"this-is-valid-1234567890-.&!+-#$%^-nA_Me", "this-is-valid-1234567890-.&!+-#$%^", "ROLE_NA_ME"},
                {"this-is-not-valid-common-name!", null, null},
                {"atLeastOneDashMustPresent", null, null}});
    }

    private static final String DISPLAY_NAME_INPUT = "Display name is unnecessary in testing purposes";
    private OrganizationRoleMappingExtractor organizationRoleMappingExtractor = new OrganizationRoleMappingExtractor();
    private List<String> invalidCommonNames = Arrays.asList(null, "", "-testija", "123-456-abc_def-",
            "this-is-not-valid-common-name!", "atLeastOneDashMustPresent");

    @Parameter
    public String commonNameInput;
    @Parameter(1)
    public String expectedOrganizationCode;
    @Parameter(2)
    public String expectedOrganizationRole;

    @Test
    public void test() {
        OrganizationRoleMapping organizationRoleMapping = organizationRoleMappingExtractor.extract(commonNameInput, DISPLAY_NAME_INPUT);
        if (organizationRoleMapping == null) {
            assertThat(invalidCommonNames.contains(commonNameInput), is(true));
        } else {
            assertThat(organizationRoleMapping.getCode(), is(equalTo(expectedOrganizationCode)));
            assertThat(organizationRoleMapping.getAuthority().getAuthority(), is(equalTo(expectedOrganizationRole)));
        }
    }
}
