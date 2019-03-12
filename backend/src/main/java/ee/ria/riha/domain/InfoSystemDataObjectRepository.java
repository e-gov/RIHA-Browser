package ee.ria.riha.domain;

import ee.ria.riha.domain.model.InfoSystemDataObject;
import ee.ria.riha.storage.client.StorageClient;
import ee.ria.riha.storage.util.Filterable;
import ee.ria.riha.storage.util.Pageable;
import ee.ria.riha.storage.util.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InfoSystemDataObjectRepository {

    private static final String DATA_OBJECT_PATH = "db/data_object_search_view";

    @Autowired
    private StorageClient storageClient;

    public PagedResponse<InfoSystemDataObject> list(Pageable pageable, Filterable filterable) {
        return storageClient.list(DATA_OBJECT_PATH, pageable, filterable, InfoSystemDataObject.class);
    }
}
