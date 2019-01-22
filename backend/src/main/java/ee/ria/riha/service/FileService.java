package ee.ria.riha.service;

import ee.ria.riha.domain.InfoSystemRepository;
import ee.ria.riha.domain.model.FileResource;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.domain.model.InfoSystemDocumentMetadata;
import ee.ria.riha.domain.model.InfoSystemFileMetadata;
import ee.ria.riha.service.auth.InfoSystemAuthorizationService;
import ee.ria.riha.service.auth.RoleType;
import ee.ria.riha.storage.domain.FileRepository;
import ee.ria.riha.storage.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ee.ria.riha.service.SecurityContextUtil.hasRole;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class FileService {

    private static final String INLINE_CONTENT_DISPOSITION_TYPE = "inline";
    private static final String ATTACHMENT_CONTENT_DISPOSITION_TYPE = "attachment";
    private static final String CONTENT_DISPOSITION_TOKEN_DELIMITER = ";";
    private static final Function<ee.ria.riha.storage.domain.model.FileResource, FileResource> STORAGE_FILE_RESOURCE_TO_FILE_RESOURCE_MODEL =
            storageFileResource -> {
                if (storageFileResource == null) {
                    return null;
                }

                return FileResource.builder()
                        .fileResourceUuid(storageFileResource.getFile_resource_uuid())
                        .fileResourceName(storageFileResource.getFile_resource_name())
                        .infoSystemUuid(storageFileResource.getInfosystem_uuid())
                        .infoSystemName(storageFileResource.getInfosystem_name())
                        .infoSystemOwnerCode(storageFileResource.getInfosystem_owner_code())
                        .infoSystemOwnerName(storageFileResource.getInfosystem_owner_name())
                        .infoSystemShortName(storageFileResource.getInfosystem_short_name())
                        .build();
            };

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private InfoSystemRepository infoSystemRepository;

    @Autowired
    private InfoSystemAuthorizationService infoSystemAuthorizationService;

    /**
     * Uploads file associated with info system
     *
     * @param inputStream         file input stream
     * @param infoSystemReference info system UUID
     * @param fileName            name of the file
     * @param contentType         MIME type of the file
     * @return UUID of created file resource
     */
    public UUID upload(InputStream inputStream, String infoSystemReference, String fileName, String contentType) {
        log.info("Uploading file '{}' to storage", fileName);

        InfoSystem infoSystem = infoSystemRepository.load(infoSystemReference);

        UUID fileUuid = fileRepository.upload(inputStream, infoSystem.getUuid(), fileName, contentType);
        log.info("File uploaded with uuid: {}", fileUuid);

        return fileUuid;
    }

    /**
     * Downloads single file associated with info system. Checks if info system description contains file link. For
     * users which are not owners or approvers, checks if file has access restrictions.
     *
     * @param infoSystemReference info system UUID
     * @param fileUuid            file resource UUID
     * @return response entity with data stream
     * @throws IOException in case of file repository errors
     */
    public ResponseEntity download(String infoSystemReference, UUID fileUuid) throws IOException {
        return download(infoSystemRepository.load(infoSystemReference), fileUuid);
    }

    /**
     * Downloads single file associated with info system. Checks if info system description contains file link. For
     * users which are not owners or approvers, checks if file has access restrictions.
     *
     * @param infoSystem info system
     * @param fileUuid   file resource UUID
     * @return response entity with data stream
     * @throws IOException in case of file repository errors
     */
    public ResponseEntity download(InfoSystem infoSystem, UUID fileUuid) throws IOException {
        checkFileAccess(infoSystem, fileUuid);

        ResponseEntity fileResource = fileRepository.download(fileUuid, infoSystem.getUuid());

        String attachmentContentDisposition = ATTACHMENT_CONTENT_DISPOSITION_TYPE
                + CONTENT_DISPOSITION_TOKEN_DELIMITER
                + getFilteredContentDisposition(fileResource.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));

        HttpHeaders headers = new HttpHeaders();
        headers.putAll(fileResource.getHeaders());
        headers.set(HttpHeaders.CONTENT_DISPOSITION, attachmentContentDisposition);

        return ResponseEntity.status(fileResource.getStatusCode())
                .headers(headers)
                .body(fileResource.getBody());
    }

    private void checkFileAccess(InfoSystem infoSystem, UUID fileUuid) {
        List<InfoSystemFileMetadata> matchingFileMetadata = getMatchingFileMetadata(infoSystem, fileUuid);
        if (matchingFileMetadata.isEmpty()) {
            throw new IllegalBrowserStateException("File is not defined in info system description");
        }

        if (metadataContainsAccessRestriction(matchingFileMetadata)
                && !(hasRole(RoleType.APPROVER)
                || (hasRole(RoleType.PRODUCER) && infoSystemAuthorizationService.isOwner(infoSystem)))) {
            throw new IllegalBrowserStateException("File access is restricted");
        }
    }

    /**
     * Filters out any content disposition type that was set by file repository
     *
     * @param contentDisposition received content disposition header value
     * @return content disposition without type, if any
     */
    private String getFilteredContentDisposition(String contentDisposition) {
        if (!StringUtils.hasText(contentDisposition)) {
            return contentDisposition;
        }

        List<String> contentDispositionTokens = Arrays.asList(StringUtils.tokenizeToStringArray(contentDisposition,
                CONTENT_DISPOSITION_TOKEN_DELIMITER));

        if (contentDispositionTokens.isEmpty()) {
            return contentDisposition;
        }

        String contentDispositionType = contentDispositionTokens.get(0).trim();

        if (contentDispositionType.equalsIgnoreCase(
                INLINE_CONTENT_DISPOSITION_TYPE) || contentDispositionType.equalsIgnoreCase(
                ATTACHMENT_CONTENT_DISPOSITION_TYPE)) {
            contentDispositionTokens.remove(0);
        }

        return contentDispositionTokens.stream().collect(Collectors.joining(CONTENT_DISPOSITION_TOKEN_DELIMITER));
    }

    private List<InfoSystemFileMetadata> getMatchingFileMetadata(InfoSystem infoSystem, UUID fileUuid) {
        return Stream.concat(infoSystem.getDocumentMetadata().stream(), infoSystem.getDataFileMetadata().stream())
                .filter(i -> i.getUrl().equalsIgnoreCase("file://" + fileUuid.toString()))
                .collect(toList());
    }

    private boolean metadataContainsAccessRestriction(List<InfoSystemFileMetadata> fileMetadata) {
        return fileMetadata.stream()
                .filter(metadata -> metadata instanceof InfoSystemDocumentMetadata)
                .map(metadata -> (InfoSystemDocumentMetadata) metadata)
                .anyMatch(InfoSystemDocumentMetadata::isAccessRestricted);
    }

    public PagedResponse<FileResource> list(Pageable pageable, CompositeFilterRequest filterRequest) {
        PagedGridResponse<ee.ria.riha.storage.domain.model.FileResource> fileResourcePagedResponse = fileRepository.list(
                filterRequest, pageable);

        return new PagedResponse<>(
                new PageRequest(fileResourcePagedResponse.getPage(), fileResourcePagedResponse.getSize()),
                fileResourcePagedResponse.getTotalElements(),
                fileResourcePagedResponse.getContent().stream()
                        .map(STORAGE_FILE_RESOURCE_TO_FILE_RESOURCE_MODEL)
                        .collect(Collectors.toList()));
    }
}
