package ee.ria.riha.web;

import ee.ria.riha.domain.model.FileResource;
import ee.ria.riha.service.FileService;
import ee.ria.riha.service.auth.PreAuthorizeInfoSystemOwnerOrReviewer;
import ee.ria.riha.service.util.ApiPageableAndCompositeRequestParams;
import ee.ria.riha.service.util.CompositeFilterRequest;
import ee.ria.riha.service.util.Pageable;
import ee.ria.riha.service.util.PagedResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;

@RestController
@Slf4j
@Api("File resources")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping(value = API_V1_PREFIX + "/systems/{reference}/files", consumes = "multipart/form-data")
    @PreAuthorizeInfoSystemOwnerOrReviewer
    @ApiOperation("Upload file")
    public ResponseEntity<UUID> upload(@PathVariable("reference") String reference,
                                 @RequestPart("file") MultipartFile file) throws IOException {
        log.info("Receiving info system '{}' file '{}' [{}] with size {}b",
                reference, file.getOriginalFilename(), file.getContentType(), file.getSize());

        UUID fileUuid = fileService.upload(file.getInputStream(), reference, file.getOriginalFilename(),
                file.getContentType());

        return ResponseEntity.ok(fileUuid);
    }

    @GetMapping(API_V1_PREFIX + "/systems/{reference}/files/{uuid}")
    @ApiOperation("Download file")
    public ResponseEntity<InputStreamResource> download(@PathVariable("reference") String reference,
                                                        @PathVariable("uuid") UUID fileUuid) throws IOException {
        log.info("Downloading info system '{}' file {}", reference, fileUuid);
        return fileService.download(reference, fileUuid);
    }

    @GetMapping(API_V1_PREFIX + "/systems/files")
    @ApiOperation("List file resources")
    @ApiPageableAndCompositeRequestParams
    public ResponseEntity<PagedResponse<FileResource>> list(CompositeFilterRequest filter, Pageable pageable) {
        return ResponseEntity.ok(fileService.list(pageable, filter));
    }

}
