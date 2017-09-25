package ee.ria.riha.service.auth;

import com.google.common.collect.ImmutableMultimap;
import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaOrganizationAwareAuthenticationToken;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.service.InfoSystemService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * @author Valentin Suhnjov
 */
@RunWith(MockitoJUnitRunner.class)
public class InfoSystemAuthorizationServiceTest {

    private static final String ACME_ORG_CODE = "777";
    private RihaOrganizationAwareAuthenticationToken authenticationToken;

    @Mock
    private InfoSystemService infoSystemService;

    @InjectMocks
    private InfoSystemAuthorizationService infoSystemAuthorization;

    private InfoSystem infoSystem = new InfoSystem("{\n" +
                                                           "  \"owner\": {\n" +
                                                           "    \"code\": \"777\"\n" +
                                                           "  }\n" +
                                                           "}");

    @Before
    public void setUp() {
        User jane = new User("janedoe", "", AuthorityUtils.NO_AUTHORITIES);
        RihaUserDetails principal = new RihaUserDetails(jane,
                                                        jane.getUsername(),
                                                        ImmutableMultimap.of(
                                                                new RihaOrganization(ACME_ORG_CODE, "Acme org"),
                                                                new SimpleGrantedAuthority("ROLE_NOT_IMPORTANT")));

        authenticationToken = new RihaOrganizationAwareAuthenticationToken(
                principal, null, null);
        authenticationToken.setActiveOrganization(ACME_ORG_CODE);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Test
    public void isOwnerReturnsFalseWhenInfoSystemIsNull() {
        assertThat(infoSystemAuthorization.isOwner((InfoSystem) null), is(false));
    }

    @Test
    public void isOwnerReturnsFalseWhenUserHasNoActiveOrganizationSet() {
        authenticationToken.setActiveOrganization(null);
        infoSystemAuthorization.isOwner(infoSystem);
    }

    @Test
    public void isOwnerReturnsTrueWhenActiveOrganizationAndInfoSystemOwnerCodeAreEqual() {
        assertThat(infoSystemAuthorization.isOwner(infoSystem), is(true));
    }

    @Test
    public void isOwnerReturnsFalseWhenInfoSystemOwnerCodeIsNotSet() {
        infoSystem = new InfoSystem("{}");
        assertThat(infoSystemAuthorization.isOwner(infoSystem), is(false));
    }

    @Test
    public void isOwnerReturnsFalseWhenActiveOrganizationAndInfoSystemOwnerCodeAreNotEqual() {
        infoSystem = new InfoSystem("{\n" +
                                            "  \"owner\": {\n" +
                                            "    \"code\": \"000\"\n" +
                                            "  }\n" +
                                            "}");
        assertThat(infoSystemAuthorization.isOwner(infoSystem), is(false));
    }

    @Test
    public void isOwnerResolvesInfoSystemByShortName() {
        infoSystemAuthorization.isOwner("not-too-short-name");

        verify(infoSystemService, times(1)).get("not-too-short-name");
    }

}