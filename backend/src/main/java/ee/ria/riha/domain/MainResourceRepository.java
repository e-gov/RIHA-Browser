package ee.ria.riha.domain;

import ee.ria.riha.client.StorageClient;
import ee.ria.riha.domain.model.MainResource;
import ee.ria.riha.service.util.Filterable;
import ee.ria.riha.service.util.Pageable;
import ee.ria.riha.service.util.PagedResponse;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Makes calls against RIHA-Storage and performs translation between main_resource resources and MainResource entities
 *
 * @author Valentin Suhnjov
 */
public class MainResourceRepository implements StorageRepository<Long, MainResource> {

    private static final String MAIN_RESOURCE_PATH = "db/main_resource";
    private static final String MAIN_RESOURCE_VIEW_PATH = "db/main_resource_view";

    private final StorageClient storageClient;

    public MainResourceRepository(StorageClient storageClient) {
        Assert.notNull(storageClient, "Storage client must be provided");
        this.storageClient = storageClient;
    }

    @Override
    public PagedResponse<MainResource> list(Pageable pageable, Filterable filterable) {
        return storageClient.list(MAIN_RESOURCE_VIEW_PATH, pageable, filterable, MainResource.class);
    }

    @Override
    public MainResource get(Long id) {
        return storageClient.get(MAIN_RESOURCE_PATH, id, MainResource.class);
    }

    @Override
    public List<MainResource> find(Filterable filterable) {
        return storageClient.find(MAIN_RESOURCE_VIEW_PATH, filterable, MainResource.class);
    }

    @Override
    public List<Long> add(MainResource mainResource) {
        return storageClient.create(MAIN_RESOURCE_PATH, mainResource);
    }

    @Override
    public void update(Long id, MainResource mainResource) {
        throw new UnsupportedOperationException("Entity can not be updated");
    }

    @Override
    public void remove(Long id) {
        throw new UnsupportedOperationException("Entity can not be deleted");
    }

}
