package ee.ria.riha.web;

import ee.ria.riha.service.FileService;
import ee.ria.riha.service.auth.PreAuthorizeInfoSystemOwnerOrReviewer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping(API_V1_PREFIX + "/systems/{reference}/files")
    @PreAuthorizeInfoSystemOwnerOrReviewer
    @ApiOperation("Upload file")
    public ResponseEntity upload(@PathVariable("reference") String infoSystemReference,
                                 @RequestParam("file") MultipartFile file) throws IOException {
        log.info("Receiving info system '{}' file '{}' [{}] with size {}b",
                infoSystemReference, file.getOriginalFilename(), file.getContentType(), file.getSize());

        UUID fileUuid = fileService.upload(file.getInputStream(), infoSystemReference, file.getOriginalFilename(),
                file.getContentType());

        return ResponseEntity.ok(fileUuid.toString());
    }

    @GetMapping(API_V1_PREFIX + "/systems/{reference}/files/{uuid}")
    @ApiOperation("Download file")
    public ResponseEntity download(@PathVariable("reference") String infoSystemReference,
                                   @PathVariable("uuid") UUID fileUuid) throws IOException {
        log.info("Downloading info system '{}' file {}", infoSystemReference, fileUuid);
        return fileService.download(infoSystemReference, fileUuid);
    }

}
