package ee.ria.riha.service;

import ee.ria.riha.model.InfoSystem;
import ee.ria.riha.service.storage.StorageClient;
import ee.ria.riha.web.util.Filterable;
import ee.ria.riha.web.util.Pageable;
import ee.ria.riha.web.util.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Valentin Suhnjov
 */
@Service
public class InfoSystemService {

    private final StorageClient storageClient;

    @Autowired
    public InfoSystemService(StorageClient storageClient) {
        this.storageClient = storageClient;
    }

    public PagedResponse<InfoSystem> list(Pageable pageable, Filterable filterable) {
        PagedResponse<InfoSystem> response = new PagedResponse<>(pageable);

        long totalElements = storageClient.count(StorageClient.MAIN_RESOURCE_PATH, filterable.getFilter());
        response.setTotalElements(totalElements);

        if (totalElements > 0) {
            List<String> descriptions = storageClient.find(StorageClient.MAIN_RESOURCE_PATH, pageable.getPageSize(),
                                                           pageable.getOffset(), filterable.getFilter(),
                                                           filterable.getSort(), filterable.getFields());
            response.setContent(descriptions.stream()
                                        .map(InfoSystem::new)
                                        .collect(Collectors.toList()));
        }

        return response;
    }

}
