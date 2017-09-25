package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.domain.InfoSystemRepository;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.storage.util.Filterable;
import ee.ria.riha.storage.util.Pageable;
import ee.ria.riha.storage.util.PagedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static ee.ria.riha.service.SecurityContextUtil.getActiveOrganization;
import static ee.ria.riha.service.SecurityContextUtil.getRihaUserDetails;

/**
 * @author Valentin Suhnjov
 */
@Service
@Slf4j
public class InfoSystemService {

    @Autowired
    private InfoSystemRepository infoSystemRepository;

    @Autowired
    private JsonValidationService infoSystemValidationService;

    public PagedResponse<InfoSystem> list(Pageable pageable, Filterable filterable) {
        return infoSystemRepository.list(pageable, filterable);
    }

    /**
     * Creates new {@link InfoSystem}. Newly created {@link InfoSystem} will be filled with owner information and
     * generated UUID.
     *
     * @param model new {@link InfoSystem} model
     * @return created {@link InfoSystem}
     */
    public InfoSystem create(InfoSystem model) {
        RihaUserDetails rihaUserDetails = getRihaUserDetails();
        if (rihaUserDetails == null) {
            throw new IllegalBrowserStateException("User must be valid RIHA logged in user");
        }

        RihaOrganization organization = getActiveOrganization();
        if (organization == null) {
            throw new IllegalBrowserStateException("Active organization must be set");
        }

        InfoSystem infoSystem = new InfoSystem(model.getJsonObject());
        infoSystem.setUuid(UUID.randomUUID());
        infoSystem.setOwnerCode(organization.getCode());
        infoSystem.setOwnerName(organization.getName());

        infoSystemValidationService.validate(infoSystem.asJson());
        log.info("User with ID {} is going to create a new IS with short name {} for next organization: {}",
                 rihaUserDetails.getPersonalCode(), infoSystem.getShortName(), infoSystem.getOwnerName());

        return infoSystemRepository.add(infoSystem);
    }

    /**
     * Retrieves {@link InfoSystem} by its short name
     *
     * @param shortName info system short name
     * @return retrieved {@link InfoSystem}
     */
    public InfoSystem get(String shortName) {
        return infoSystemRepository.load(shortName);
    }

    /**
     * Retrieves {@link InfoSystem} by its short name
     *
     * @param uuid info system uuid
     * @return retrieved {@link InfoSystem}
     */
    public InfoSystem get(UUID uuid) {
        return infoSystemRepository.load(uuid);
    }

    /**
     * Creates new record with the same UUID and owner. Other parts of {@link InfoSystem} are updated from model.
     *
     * @param shortName info system short name
     * @param model     updated {@link InfoSystem} model
     * @return new {@link InfoSystem}
     */
    public InfoSystem update(String shortName, InfoSystem model) {
        RihaUserDetails rihaUserDetails = getRihaUserDetails();
        if (rihaUserDetails == null) {
            throw new IllegalBrowserStateException("User must be valid RIHA logged in user");
        }
        InfoSystem existingInfoSystem = get(shortName);

        InfoSystem updatedInfoSystem = new InfoSystem(model.getJsonObject());
        updatedInfoSystem.setUuid(existingInfoSystem.getUuid());
        updatedInfoSystem.setOwnerCode(existingInfoSystem.getOwnerCode());
        updatedInfoSystem.setOwnerName(existingInfoSystem.getOwnerName());

        infoSystemValidationService.validate(updatedInfoSystem.asJson());
        log.info("User with ID {} is going to update an IS with ID {} and short name {} for next organization: {}",
                 rihaUserDetails.getPersonalCode(), existingInfoSystem.getId(), shortName,
                 existingInfoSystem.getOwnerName());

        return infoSystemRepository.add(updatedInfoSystem);
    }

}
