package ee.ria.riha.service;

import ee.ria.riha.storage.domain.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public UUID upload(InputStream inputStream, String fileName, String contentType) {
        log.info("Uploading file '{}' to storage", fileName);

        UUID fileUuid = fileRepository.upload(inputStream, fileName, contentType);
        log.info("File uploaded with uuid: {}", fileUuid);

        return fileUuid;
    }

    public ResponseEntity download(UUID fileUuid) throws IOException {
        return fileRepository.download(fileUuid);
    }

}
