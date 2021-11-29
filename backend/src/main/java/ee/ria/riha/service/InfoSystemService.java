package ee.ria.riha.service;

import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.RihaUserDetails;
import ee.ria.riha.domain.InfoSystemRepository;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.domain.model.Issue;
import ee.ria.riha.logging.auditlog.AuditEvent;
import ee.ria.riha.logging.auditlog.AuditLogger;
import ee.ria.riha.logging.auditlog.AuditType;
import ee.ria.riha.storage.util.FilterRequest;
import ee.ria.riha.storage.util.Filterable;
import ee.ria.riha.storage.util.Pageable;
import ee.ria.riha.storage.util.PagedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static ee.ria.riha.service.SecurityContextUtil.getActiveOrganization;
import static ee.ria.riha.service.SecurityContextUtil.getRihaUserDetails;

/**
 * @author Valentin Suhnjov
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class InfoSystemService {

    private static final String NOT_SET_VALUE = "[NOT SET]";

    @Autowired
    private InfoSystemRepository infoSystemRepository;

    @Autowired
    private JsonValidationService infoSystemValidationService;
    private IssueService issueService;

    public final AuditLogger auditLogger;

    private DateTimeFormatter isoDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");


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
        RihaOrganization organization = getActiveOrganization()
                .orElseThrow(() -> new IllegalBrowserStateException("Unable to retrieve active organization"));
        validateInfoSystemShortName(model.getShortName());

        log.info("User '{}' with active organization '{}' is creating new info system with short name '{}'",
                getRihaUserDetails().map(RihaUserDetails::getPersonalCode).orElse(NOT_SET_VALUE),
                organization,
                model.getShortName());

        auditLogger.log(AuditEvent.CREATE, AuditType.INFOSYSTEM, model);

        model.setUuid(UUID.randomUUID());
        model.setOwnerCode(organization.getCode());
        model.setOwnerName(organization.getName());
        String creationTimestamp = isoDateTimeFormatter.format(ZonedDateTime.now());
        model.setCreationTimestamp(creationTimestamp);
        model.setUpdateTimestamp(creationTimestamp);

        infoSystemValidationService.validate(model.getJsonContent());

        return infoSystemRepository.add(model);
    }

    private void validateInfoSystemShortName(String shortName) {
        log.debug("Checking info system '{}' existence", shortName);
        FilterRequest filter = new FilterRequest("short_name,=," + shortName, null, null);
        List<InfoSystem> infoSystems = infoSystemRepository.find(filter);
        if (!infoSystems.isEmpty()) {
            throw new ValidationException("validation.system.shortNameAlreadyTaken", shortName);
        }
    }

    /**
     * Retrieves {@link InfoSystem} by issue id
     *
     * @param issueId - issue id
     * @return retrieved {@link InfoSystem}
     */
    public InfoSystem getByIssueId(Long issueId) {
        Issue issue = issueService.getIssueById(issueId);
        return get(issue.getInfoSystemUuid());
    }

    /**
     * Retrieves {@link InfoSystem} by its reference. Reference may be either info system UUID or short name.
     *
     * @param reference info system reference
     * @return retrieved {@link InfoSystem}
     */
    public InfoSystem get(String reference) {
        return infoSystemRepository.load(reference);
    }

    /**
     * Convenience method for retrieving {@link InfoSystem} by its UUID
     *
     * @param uuid info system uuid
     * @return retrieved {@link InfoSystem}
     */
    public InfoSystem get(UUID uuid) {
        return get(uuid.toString());
    }

    /**
     * Creates new record with the same UUID and owner. Other parts of {@link InfoSystem} are updated from model.
     *
     * @param reference info system reference
     * @param model     updated {@link InfoSystem} model
     * @return new {@link InfoSystem}
     */
    public InfoSystem update(String reference, InfoSystem model) {
        InfoSystem existingInfoSystem = get(reference);
        log.info("User '{}' with active organization '{}'" +
                        " is updating info system with id {}, owner code '{}' and short name '{}'",
                getRihaUserDetails().map(RihaUserDetails::getPersonalCode).orElse(NOT_SET_VALUE),
                getActiveOrganization().orElse(new RihaOrganization(NOT_SET_VALUE, NOT_SET_VALUE)),
                existingInfoSystem.getId(),
                existingInfoSystem.getOwnerCode(),
                existingInfoSystem.getShortName());

        auditLogger.log(AuditEvent.UPDATE, AuditType.INFOSYSTEM, model);

        if (!existingInfoSystem.getShortName().equals(model.getShortName())) {
            validateInfoSystemShortName(model.getShortName());
        }
        model.setUuid(existingInfoSystem.getUuid());
        model.setOwnerCode(existingInfoSystem.getOwnerCode());
        model.setOwnerName(existingInfoSystem.getOwnerName());
        model.setCreationTimestamp(existingInfoSystem.getCreationTimestamp());
        model.setUpdateTimestamp(isoDateTimeFormatter.format(ZonedDateTime.now()));

        model.setCreationAndUpdateTimestampToFilesMetadata(existingInfoSystem);

        infoSystemValidationService.validate(model.getJsonContent());

        return infoSystemRepository.add(model);
    }

    @Autowired
    public void setIssueService(IssueService issueService) {
        this.issueService = issueService;
    }
}
