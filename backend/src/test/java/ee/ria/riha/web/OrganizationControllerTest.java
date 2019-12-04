package ee.ria.riha.web;

import ee.ria.riha.storage.util.CompositeFilterRequest;
import ee.ria.riha.web.model.UserDetailsModel;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

public class OrganizationControllerTest {

    @Test
    public void sortUsers() {

        List<UserDetailsModel> testUsers = Arrays.asList(
                UserDetailsModel.builder().email("test0@test.aa").build(),
                UserDetailsModel.builder().email(null).build(),
                UserDetailsModel.builder().email("test1@test.aa").build(),
                UserDetailsModel.builder().email("test2@test.aa").build()
        );
        CompositeFilterRequest filterRequest = new CompositeFilterRequest(Collections.emptyList(), Collections.singletonList("asc"));

        OrganizationController.sortUsers(testUsers, OrganizationController.USER_DETAILS_COMPARATORS.get("email"),false);
        assertThat(testUsers.get(3).getEmail(), nullValue());

        OrganizationController.sortUsers(testUsers, OrganizationController.USER_DETAILS_COMPARATORS.get("email"),true);
        assertThat(testUsers.get(0).getEmail(), nullValue());
    }
}