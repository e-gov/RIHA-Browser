package ee.ria.riha.web;

import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.service.JaneAuthenticationTokenBuilder;
import ee.ria.riha.web.model.InfoSystemModel;
import org.junit.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

/**
 * @author Valentin Suhnjov
 */
public class InfoSystemModelMapperTest {

    private static final String CONTACTS_KEY = "contacts";

    private final InfoSystem infoSystemWithContacts = new InfoSystem(
            "{\n" +
                    "  \"some_property\": \"value\",\n" +
                    "  \"contacts\": [\n" +
                    "    {\n" +
                    "      \"name\": \"John Doe\",\n" +
                    "      \"email\": \"john@nowhere.com\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}");

    private InfoSystemModelMapper infoSystemModelMapper = new InfoSystemModelMapper();

    @Test
    public void doesNotRemoveContactsIfUserIsAuthenticated() {
        SecurityContextHolder.getContext().setAuthentication(JaneAuthenticationTokenBuilder.builder().build());

        InfoSystemModel model = infoSystemModelMapper.map(infoSystemWithContacts);

        assertThat(model.getDetails(), containsString(CONTACTS_KEY));
    }

    @Test
    public void doesNotMapContactsIfUserIsNotAuthenticated() {
        SecurityContextHolder.getContext().setAuthentication(null);

        InfoSystemModel model = infoSystemModelMapper.map(infoSystemWithContacts);

        assertThat(model.getDetails(), not(containsString(CONTACTS_KEY)));
    }

}