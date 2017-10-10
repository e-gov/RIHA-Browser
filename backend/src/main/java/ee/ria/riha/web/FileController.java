package ee.ria.riha.web;

import ee.ria.riha.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;

@RestController
@RequestMapping(API_V1_PREFIX + "/files")
@Slf4j
@Api("File resources")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_KIRJELDAJA')")
    @ApiOperation("Upload file")
    public ResponseEntity upload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("Receiving file '{}' [{}] with size {}b",
                file.getOriginalFilename(), file.getContentType(), file.getSize());

        UUID fileUuid = fileService.upload(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
        return ResponseEntity.ok(fileUuid.toString());
    }

}
