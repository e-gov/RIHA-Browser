package ee.ria.riha.web;

import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.domain.model.IssueType;
import ee.ria.riha.rules.CleanAuthentication;
import ee.ria.riha.service.JaneAuthenticationTokenBuilder;
import ee.ria.riha.web.model.InfoSystemModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * @author Valentin Suhnjov
 */
public class InfoSystemModelMapperTest {

    private static final String CONTACTS_KEY = "contacts";

    private final InfoSystem mappedInfoSystem = new InfoSystem();

    @Rule
    public CleanAuthentication cleanAuthentication = new CleanAuthentication();

    private InfoSystemModelMapper infoSystemModelMapper = new InfoSystemModelMapper();

    @Before
    public void setUp() {
        mappedInfoSystem.setId(123L);
        mappedInfoSystem.addContact("John Doe", "john@example.com");
        mappedInfoSystem.setLastPositiveApprovalRequestType(IssueType.TAKE_INTO_USE_REQUEST);
        mappedInfoSystem.setLastPositiveApprovalRequestDate(new Date());
        mappedInfoSystem.setLastPositiveEstablishmentRequestDate(new Date());
        mappedInfoSystem.setLastPositiveTakeIntoUseRequestDate(new Date());
        mappedInfoSystem.setLastPositiveFinalizationRequestDate(new Date());
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

}