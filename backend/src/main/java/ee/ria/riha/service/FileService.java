package ee.ria.riha.service;

import ee.ria.riha.storage.domain.FileRepository;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileService {

    private static final String INLINE_CONTENT_DISPOSITION_TYPE = "inline";
    private static final String ATTACHMENT_CONTENT_DISPOSITION_TYPE = "attachment";
    private static final String CONTENT_DISPOSITION_TOKEN_DELIMITER = ";";

    @Autowired
    private FileRepository fileRepository;

    public UUID upload(InputStream inputStream, String fileName, String contentType) {
        log.info("Uploading file '{}' to storage", fileName);

        UUID fileUuid = fileRepository.upload(inputStream, fileName, contentType);
        log.info("File uploaded with uuid: {}", fileUuid);

        return fileUuid;
    }

    public ResponseEntity download(UUID fileUuid) throws IOException {
        ResponseEntity fileResource = fileRepository.download(fileUuid);

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

}
