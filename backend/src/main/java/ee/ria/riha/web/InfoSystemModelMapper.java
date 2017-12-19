package ee.ria.riha.web;

import com.fasterxml.jackson.databind.node.ObjectNode;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.web.model.InfoSystemModel;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static ee.ria.riha.service.SecurityContextUtil.isUserAuthenticated;

/**
 * Maps {@link InfoSystem} to {@link InfoSystemModel}.
 *
 * @author Valentin Suhnjov
 */
@Component
public class InfoSystemModelMapper implements ModelMapper<InfoSystem, InfoSystemModel> {

    private static final List<String> EVICTED_FIRST_LEVEL_PROPERTIES = Collections.singletonList("contacts");

    /**
     * Maps {@link InfoSystem} to {@link InfoSystemModel} performing additional transformations that depend on user
     * authentication. Unauthenticated users will not see some json properties like contacts.
     *
     * @param infoSystem an info system to be mapped
     * @return info system model
     */
    @Override
    public InfoSystemModel map(InfoSystem infoSystem) {
        InfoSystem filteredInfoSystem = getFilteredInfoSystem(infoSystem);

        InfoSystemModel model = new InfoSystemModel();
        model.setId(filteredInfoSystem.getId());
        model.setJson(filteredInfoSystem.getJsonContent());

        return model;
    }

    private InfoSystem getFilteredInfoSystem(InfoSystem infoSystem) {
        if (isUserAuthenticated()) {
            return infoSystem;
        }

        InfoSystem infoSystemCopy = infoSystem.copy();
        ((ObjectNode) infoSystemCopy.getJsonContent()).remove(EVICTED_FIRST_LEVEL_PROPERTIES);

        return infoSystemCopy;
    }

}
