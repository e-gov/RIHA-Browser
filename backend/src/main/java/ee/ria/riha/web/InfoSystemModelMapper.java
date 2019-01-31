package ee.ria.riha.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.service.auth.InfoSystemAuthorizationService;
import ee.ria.riha.service.auth.RoleType;
import ee.ria.riha.web.model.InfoSystemModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ee.ria.riha.service.SecurityContextUtil.hasRole;
import static ee.ria.riha.service.SecurityContextUtil.isUserAuthenticated;

/**
 * Maps {@link InfoSystem} to {@link InfoSystemModel}.
 *
 * @author Valentin Suhnjov
 */
@Component
public class InfoSystemModelMapper implements ModelMapper<InfoSystem, InfoSystemModel> {

    private static final List<String> INFO_SYSTEM_CONTACTS = Collections.singletonList("contacts");
    private static final List<String> LATEST_AUDIT_DETAILS = Arrays.asList("latest_audit_date",
            "latest_audit_resolution");
    private static final String SECURITY_DETAILS_KEY = "security";
    private InfoSystemAuthorizationService infoSystemAuthorizationService;

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
        model.setLastPositiveApprovalRequestType(filteredInfoSystem.getLastPositiveApprovalRequestType());
        model.setLastPositiveApprovalRequestDate(filteredInfoSystem.getLastPositiveApprovalRequestDate());
        model.setLastPositiveEstablishmentRequestDate(filteredInfoSystem.getLastPositiveEstablishmentRequestDate());
        model.setLastPositiveTakeIntoUseRequestDate(filteredInfoSystem.getLastPositiveTakeIntoUseRequestDate());
        model.setLastPositiveFinalizationRequestDate(filteredInfoSystem.getLastPositiveFinalizationRequestDate());
        model.setHasUsedSystemTypeRelations(infoSystem.isHasUsedSystemTypeRelations());

        return model;
    }

    private InfoSystem getFilteredInfoSystem(InfoSystem infoSystem) {
        InfoSystem infoSystemCopy = infoSystem.copy();

        removeLatestAuditDetailsIfUserIsNeitherInfoSystemOwnerNorApprover(infoSystemCopy);
        removeInfoSystemContactsIfUserIsNotAuthenticated(infoSystemCopy);

        return infoSystemCopy;
    }

    private void removeLatestAuditDetailsIfUserIsNeitherInfoSystemOwnerNorApprover(InfoSystem infoSystem) {
        if (infoSystemAuthorizationService.isOwner(infoSystem) || hasRole(RoleType.APPROVER)) {
            return;
        }
        
        JsonNode securityDetails = infoSystem.getJsonContent().path(SECURITY_DETAILS_KEY);
        if (securityDetails.isMissingNode()) {
            return;
        }

        ((ObjectNode) securityDetails).remove(LATEST_AUDIT_DETAILS);
    }

    private void removeInfoSystemContactsIfUserIsNotAuthenticated(InfoSystem infoSystem) {
        if (isUserAuthenticated()) {
            return;
        }

        ((ObjectNode) infoSystem.getJsonContent()).remove(INFO_SYSTEM_CONTACTS);
    }

    @Autowired
    public void setInfoSystemAuthorizationService(InfoSystemAuthorizationService infoSystemAuthorizationService) {
        this.infoSystemAuthorizationService = infoSystemAuthorizationService;
    }
}
