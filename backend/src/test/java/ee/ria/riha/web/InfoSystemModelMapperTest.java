package ee.ria.riha.web;

import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.rules.CleanAuthentication;
import ee.ria.riha.service.JaneAuthenticationTokenBuilder;
import ee.ria.riha.web.model.InfoSystemModel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Valentin Suhnjov
 */
public class InfoSystemModelMapperTest {

    private static final String CONTACTS_KEY = "contacts";

    private final InfoSystem infoSystemWithContacts = new InfoSystem();

    @Rule
    public CleanAuthentication cleanAuthentication = new CleanAuthentication();

    private InfoSystemModelMapper infoSystemModelMapper = new InfoSystemModelMapper();

    @Before
    public void setUp() {
        infoSystemWithContacts.addContact("John Doe", "john@example.com");
    }

    @Test
    public void doesNotRemoveContactsIfUserIsAuthenticated() {
        SecurityContextHolder.getContext().setAuthentication(JaneAuthenticationTokenBuilder.builder().build());

        InfoSystemModel model = infoSystemModelMapper.map(infoSystemWithContacts);

        assertThat(model.getJson().path(CONTACTS_KEY).isMissingNode(), is(false));
    }

    @Test
    public void doesNotMapContactsIfUserIsNotAuthenticated() {
        SecurityContextHolder.getContext().setAuthentication(null);

        InfoSystemModel model = infoSystemModelMapper.map(infoSystemWithContacts);

        assertThat(model.getJson().path(CONTACTS_KEY).isMissingNode(), is(true));
    }

}