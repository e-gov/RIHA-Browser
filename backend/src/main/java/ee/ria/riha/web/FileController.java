package ee.ria.riha.web;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;

import ee.ria.riha.domain.model.FileResource;
import ee.ria.riha.service.FileService;
import ee.ria.riha.service.auth.PreAuthorizeInfoSystemOwnerOrReviewer;
import ee.ria.riha.service.util.ApiPageableAndCompositeRequestParams;
import ee.ria.riha.service.util.CompositeFilterRequest;
import ee.ria.riha.service.util.Pageable;
import ee.ria.riha.service.util.PagedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@Tag(name = "File resources")
public class FileController {

  @Autowired private FileService fileService;

  @PostMapping(
      value = API_V1_PREFIX + "/systems/{reference}/files",
      consumes = "multipart/form-data")
  @PreAuthorizeInfoSystemOwnerOrReviewer
  @Operation(summary = "Upload file")
  public ResponseEntity<UUID> upload(
      @PathVariable("reference") String reference, @RequestPart("file") MultipartFile file)
      throws IOException {
    log.info(
        "Receiving info system '{}' file '{}' [{}] with size {}b",
        reference,
        file.getOriginalFilename(),
        file.getContentType(),
        file.getSize());

    UUID fileUuid =
        fileService.upload(
            file.getInputStream(), reference, file.getOriginalFilename(), file.getContentType());

    return ResponseEntity.ok(fileUuid);
  }

  @GetMapping(API_V1_PREFIX + "/systems/{reference}/files/{uuid}")
  @Operation(summary = "Download file")
  public ResponseEntity<InputStreamResource> download(
      @PathVariable("reference") String reference, @PathVariable("uuid") UUID fileUuid)
      throws IOException {
    log.info("Downloading info system '{}' file {}", reference, fileUuid);
    return fileService.download(reference, fileUuid);
  }

  @GetMapping(API_V1_PREFIX + "/systems/files")
  @Operation(summary = "List file resources")
  @ApiPageableAndCompositeRequestParams
  public ResponseEntity<PagedResponse<FileResource>> list(
      CompositeFilterRequest filter, Pageable pageable) {
    return ResponseEntity.ok(fileService.list(pageable, filter));
  }
}
