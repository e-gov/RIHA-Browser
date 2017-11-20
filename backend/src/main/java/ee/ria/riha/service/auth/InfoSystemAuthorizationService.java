package ee.ria.riha.service.auth;

import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.service.InfoSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static ee.ria.riha.service.SecurityContextUtil.getActiveOrganization;

/**
 * Provides info system authorization methods
 *
 * @author Valentin Suhnjov
 */
@Service
public class InfoSystemAuthorizationService {

    @Autowired
    private InfoSystemService infoSystemService;

    /**
     * Checks if current user belongs to organization that is owner of the info system with specified short name.
     * Returns true if and only if current user active organization code is same as info system owner.code.
     *
     * @param shortName info system short name
     * @return true when current user active organization code is same as info system owner code, false otherwise
     */
    public boolean isOwner(String shortName) {
        return isOwner(infoSystemService.get(shortName));
    }

    /**
     * Checks if current user belongs to organization that is owner of this particular info system. Returns true if and
     * only if current user active organization code is same as info system owner.code.
     *
     * @param infoSystem info system
     * @return true when current user active organization code is same as info system owner code, false otherwise
     */
    public boolean isOwner(InfoSystem infoSystem) {
        if (infoSystem == null) {
            return false;
        }

        String ownerCode = infoSystem.getOwnerCode();
        if (!StringUtils.hasText(ownerCode)) {
            return false;
        }

        return getActiveOrganization()
                .map(RihaOrganization::getCode)
                .map(ownerCode::equals)
                .orElse(false);
    }
}
