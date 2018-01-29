package ee.ria.riha.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import ee.ria.riha.authentication.RihaOrganizationAwareAuthenticationToken;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.domain.model.IssueType;
import ee.ria.riha.rules.CleanAuthentication;
import ee.ria.riha.service.JaneAuthenticationTokenBuilder;
import ee.ria.riha.service.auth.InfoSystemAuthorizationService;
import ee.ria.riha.service.auth.RoleType;
import ee.ria.riha.web.model.InfoSystemModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Valentin Suhnjov
 */
@RunWith(MockitoJUnitRunner.class)
public class InfoSystemModelMapperTest {

    private static final String CONTACTS_KEY = "contacts";
    private static final String SECURITY_DETAILS_KEY = "security";
    private static final String LATEST_AUDIT_DATE_KEY = "latest_audit_date";
    private static final String LATEST_AUDIT_RESOLUTION_KEY = "latest_audit_resolution";
    private static final String ACME_ORG_OWNER_CODE = "555010203";

    private static final String SECURITY_DETAILS_JSON = "{\n" +
            "  \"security\": " +
            "  {\n" +
            "    \"standard\": \"ISKE\",\n" +
            "    \"class\": \"K1T2S1\",\n" +
            "    \"level\": \"M\",\n" +
            "    \"latest_audit_date\": \"2017-11-13T17:15:55.002+02:00\",\n" +
            "    \"latest_audit_resolution\": \"Auditeeritud märkuste või soovitustega\"\n" +
            "  }\n" +
            "}";

    private InfoSystem mappedInfoSystem;

    @Rule
    public CleanAuthentication cleanAuthentication = new CleanAuthentication();

    @Mock
    private InfoSystemAuthorizationService infoSystemAuthorizationService;
    @InjectMocks
    private InfoSystemModelMapper infoSystemModelMapper = new InfoSystemModelMapper();

    @Before
    public void setUp() {
        mappedInfoSystem = new InfoSystem(fromString(SECURITY_DETAILS_JSON));
        mappedInfoSystem.setId(123L);
        mappedInfoSystem.addContact("John Doe", "john@example.com");
        mappedInfoSystem.setLastPositiveApprovalRequestType(IssueType.TAKE_INTO_USE_REQUEST);
        mappedInfoSystem.setLastPositiveApprovalRequestDate(new Date());
        mappedInfoSystem.setLastPositiveEstablishmentRequestDate(new Date());
        mappedInfoSystem.setLastPositiveTakeIntoUseRequestDate(new Date());
        mappedInfoSystem.setLastPositiveFinalizationRequestDate(new Date());

        when(infoSystemAuthorizationService.isOwner(any(InfoSystem.class))).thenCallRealMethod();
    }

    @Test
    public void doesNotRemoveContactsIfUserIsAuthenticated() {
        SecurityContextHolder.getContext().setAuthentication(JaneAuthenticationTokenBuilder.builder().build());

        InfoSystemModel model = infoSystemModelMapper.map(mappedInfoSystem);

        assertThat(model.getJson().path(CONTACTS_KEY).isMissingNode(), is(false));
    }

    @Test
    public void doesNotMapContactsIfUserIsNotAuthenticated() {
        SecurityContextHolder.getContext().setAuthentication(null);

        InfoSystemModel model = infoSystemModelMapper.map(mappedInfoSystem);

        assertThat(model.getJson().path(CONTACTS_KEY).isMissingNode(), is(true));
    }

    @Test
    public void doesNotMapLatestAuditDetailsIfUserIsNotAuthenticated() {
        SecurityContextHolder.getContext().setAuthentication(null);

        InfoSystemModel model = infoSystemModelMapper.map(mappedInfoSystem);
        assertThat(model.getJson().path(SECURITY_DETAILS_KEY).path(LATEST_AUDIT_DATE_KEY).isMissingNode(), is(true));
        assertThat(model.getJson().path(SECURITY_DETAILS_KEY).path(LATEST_AUDIT_RESOLUTION_KEY).isMissingNode(), is(true));
    }

    @Test
    public void doesNotMapLatestAuditDetailsIfUserIsNeitherOwnerNorApprover() {
        SecurityContextHolder.getContext().setAuthentication(JaneAuthenticationTokenBuilder.builder().build());

        InfoSystemModel model = infoSystemModelMapper.map(mappedInfoSystem);
        assertThat(model.getJson().path(SECURITY_DETAILS_KEY).path(LATEST_AUDIT_DATE_KEY).isMissingNode(), is(true));
        assertThat(model.getJson().path(SECURITY_DETAILS_KEY).path(LATEST_AUDIT_RESOLUTION_KEY).isMissingNode(), is(true));
    }

    @Test
    public void doesNotRemoveAuditDetailsIfUserIsInfoSystemOwner() {
        mappedInfoSystem.setOwnerCode(ACME_ORG_OWNER_CODE);

        RihaOrganizationAwareAuthenticationToken janeAuthenticationToken = JaneAuthenticationTokenBuilder.builder().build();
        janeAuthenticationToken.setActiveOrganization(ACME_ORG_OWNER_CODE);
        SecurityContextHolder.getContext().setAuthentication(janeAuthenticationToken);

        InfoSystemModel model = infoSystemModelMapper.map(mappedInfoSystem);
        assertThat(model.getJson().path(SECURITY_DETAILS_KEY).path(LATEST_AUDIT_DATE_KEY).isMissingNode(), is(false));
        assertThat(model.getJson().path(SECURITY_DETAILS_KEY).path(LATEST_AUDIT_RESOLUTION_KEY).isMissingNode(), is(false));
    }

    @Test
    public void doesNotRemoveAuditDetailsIfUserHasApproverRole() {
        JaneAuthenticationTokenBuilder janeAuthenticationTokenBuilder = JaneAuthenticationTokenBuilder.builder();
        janeAuthenticationTokenBuilder.setBaseAuthorities(Collections.singletonList(new SimpleGrantedAuthority(RoleType.APPROVER.getRole())));
        SecurityContextHolder.getContext().setAuthentication(janeAuthenticationTokenBuilder.build());

        InfoSystemModel model = infoSystemModelMapper.map(mappedInfoSystem);
        assertThat(model.getJson().path(SECURITY_DETAILS_KEY).path(LATEST_AUDIT_DATE_KEY).isMissingNode(), is(false));
        assertThat(model.getJson().path(SECURITY_DETAILS_KEY).path(LATEST_AUDIT_RESOLUTION_KEY).isMissingNode(), is(false));
    }

    @Test
    public void mapsNonJsonProperties() {
        InfoSystemModel map = infoSystemModelMapper.map(mappedInfoSystem);

        assertThat(map.getId(), equalTo(mappedInfoSystem.getId()));
        assertThat(map.getLastPositiveApprovalRequestDate(),
                equalTo(mappedInfoSystem.getLastPositiveApprovalRequestDate()));
        assertThat(map.getLastPositiveApprovalRequestType(),
                equalTo(mappedInfoSystem.getLastPositiveApprovalRequestType()));
        assertThat(map.getLastPositiveEstablishmentRequestDate(),
                equalTo(mappedInfoSystem.getLastPositiveEstablishmentRequestDate()));
        assertThat(map.getLastPositiveTakeIntoUseRequestDate(),
                equalTo(mappedInfoSystem.getLastPositiveTakeIntoUseRequestDate()));
        assertThat(map.getLastPositiveFinalizationRequestDate(),
                equalTo(mappedInfoSystem.getLastPositiveFinalizationRequestDate()));
    }

    private JsonNode fromString(String json) {
        try {
            return JsonLoader.fromString(json);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}