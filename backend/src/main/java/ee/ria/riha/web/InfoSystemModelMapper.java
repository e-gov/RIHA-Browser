package ee.ria.riha.web;

import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.web.model.InfoSystemModel;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ee.ria.riha.service.SecurityContextUtil.isUserAuthenticated;

/**
 * Maps {@link InfoSystem} to {@link InfoSystemModel}.
 *
 * @author Valentin Suhnjov
 */
@Component
public class InfoSystemModelMapper {

    /**
     * Maps {@link InfoSystem} to {@link InfoSystemModel} performing additional transformations that depend on user
     * authentication. Unauthenticated users will not see some json properties like contacts.
     *
     * @param infoSystem an info system to be mapped
     * @return info system model
     */
    public InfoSystemModel map(InfoSystem infoSystem) {
        InfoSystem filteredInfoSystem = getFilteredInfoSystem(infoSystem);

        InfoSystemModel model = new InfoSystemModel();
        model.setId(filteredInfoSystem.getId());
        model.setJson(filteredInfoSystem.getJsonObject().toString());

        return model;
    }

    private InfoSystem getFilteredInfoSystem(InfoSystem infoSystem) {
        if (isUserAuthenticated()) {
            return infoSystem;
        }

        JSONObject originalJsonObject = infoSystem.getJsonObject();
        List<String> filteredNames = Arrays.stream(JSONObject.getNames(originalJsonObject))
                .filter(getEvictedNamesPredicate().negate())
                .collect(Collectors.toList());

        JSONObject shallowJsonObjectCopy = new JSONObject(originalJsonObject, filteredNames.toArray(new String[0]));
        return new InfoSystem(shallowJsonObjectCopy);
    }

    private Predicate<String> getEvictedNamesPredicate() {
        return name -> name.equalsIgnoreCase("contacts");
    }

}
