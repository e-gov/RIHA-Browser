package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.domain.InfoSystemRepository;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.domain.model.Issue;
import ee.ria.riha.storage.util.FilterRequest;
import ee.ria.riha.storage.util.Filterable;
import ee.ria.riha.storage.util.Pageable;
import ee.ria.riha.storage.util.PagedResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static ee.ria.riha.service.SecurityContextUtil.getActiveOrganization;
import static ee.ria.riha.service.SecurityContextUtil.getRihaUserPersonalCode;

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
    private IssueService issueService;

    private DateTimeFormatter isoDateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

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
        RihaOrganization organization = getActiveOrganization();
        if (organization == null) {
            throw new ValidationException("validation.generic.activeOrganization.notSet");
        }

        InfoSystem infoSystem = new InfoSystem(model.getJsonObject());
        log.info("User '{}' with active organization '{}'" +
                        " is creating new info system with short name '{}'",
                getRihaUserPersonalCode(), organization, infoSystem.getShortName());
        validateInfoSystemShortName(infoSystem.getShortName());
        infoSystem.setUuid(UUID.randomUUID());
        infoSystem.setOwnerCode(organization.getCode());
        infoSystem.setOwnerName(organization.getName());
        String creationTimestamp = isoDateTimeFormatter.format(ZonedDateTime.now());
        infoSystem.setCreationTimestamp(creationTimestamp);
        infoSystem.setUpdateTimestamp(creationTimestamp);

        infoSystemValidationService.validate(infoSystem.asJson());

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
     * Retrieves {@link InfoSystem} by issue id
     *
     * @param issueId - issue id
     * @return retrieved {@link InfoSystem}
     */
    public InfoSystem get(Long issueId) {
        Issue issue = issueService.getIssueById(issueId);
        return get(issue.getInfoSystemUuid());
    }

    /**
     * Creates new record with the same UUID and owner. Other parts of {@link InfoSystem} are updated from model.
     *
     * @param shortName info system short name
     * @param model     updated {@link InfoSystem} model
     * @return new {@link InfoSystem}
     */
    public InfoSystem update(String shortName, InfoSystem model) {
        InfoSystem existingInfoSystem = get(shortName);
        log.info("User '{}' with active organization '{}'" +
                        " is updating info system with id {}, owner code '{}' and short name '{}'",
                getRihaUserPersonalCode(), getActiveOrganization(), existingInfoSystem.getId(),
                existingInfoSystem.getOwnerCode(), existingInfoSystem.getShortName());

        InfoSystem updatedInfoSystem = new InfoSystem(model.getJsonObject());
        if (!shortName.equals(updatedInfoSystem.getShortName())) {
            validateInfoSystemShortName(updatedInfoSystem.getShortName());
        }
        updatedInfoSystem.setUuid(existingInfoSystem.getUuid());
        updatedInfoSystem.setOwnerCode(existingInfoSystem.getOwnerCode());
        updatedInfoSystem.setOwnerName(existingInfoSystem.getOwnerName());
        updatedInfoSystem.setCreationTimestamp(existingInfoSystem.getCreationTimestamp());
        updatedInfoSystem.setUpdateTimestamp(isoDateTimeFormatter.format(ZonedDateTime.now()));

        infoSystemValidationService.validate(updatedInfoSystem.asJson());

        return infoSystemRepository.add(updatedInfoSystem);
    }

    /**
     * Extracts info system contacts from JSON.
     *
     * @param infoSystem - info system object
     * @return array of info system contacts
     */
    public String[] getSystemContacts(InfoSystem infoSystem) {
        JSONArray jsonContactsArray = infoSystem.getJsonObject().optJSONArray("contacts");
        if (jsonContactsArray == null || jsonContactsArray.length() == 0) {
            return new String[0];
        }

        String[] contacts = new String[jsonContactsArray.length()];
        for (int i = 0; i < jsonContactsArray.length(); i++) {
            contacts[i] = jsonContactsArray.optJSONObject(i).optString("email");
        }
        return contacts;
    }

    private void validateInfoSystemShortName(String shortName) {
        log.debug("Checking info system '{}' existence", shortName);
        FilterRequest filter = new FilterRequest("short_name,=," + shortName, null, null);
        List<InfoSystem> infoSystems = infoSystemRepository.find(filter);
        if (!infoSystems.isEmpty()) {
            throw new ValidationException("validation.system.shortName.alreadyTaken", shortName);
        }
    }

    @Autowired
    public void setIssueService(IssueService issueService) {
        this.issueService = issueService;
    }
}
