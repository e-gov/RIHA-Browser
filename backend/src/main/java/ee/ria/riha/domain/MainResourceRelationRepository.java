package ee.ria.riha.domain;

import ee.ria.riha.client.StorageClient;
import ee.ria.riha.domain.model.MainResourceRelation;
import ee.ria.riha.service.util.Filterable;
import ee.ria.riha.service.util.Pageable;
import ee.ria.riha.service.util.PagedResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Makes calls against RIHA-Storage and performs various operations on {@link MainResourceRelation} entities.
 *
 * @author Valentin Suhnjov
 */
public class MainResourceRelationRepository implements StorageRepository<Long, MainResourceRelation> {

    private static final String MAIN_RESOURCE_RELATION_PATH = "db/main_resource_relation";
    private static final String MAIN_RESOURCE_RELATION_VIEW_PATH = "db/main_resource_relation_view";

    private final StorageClient storageClient;

    public MainResourceRelationRepository(StorageClient storageClient) {
        Assert.notNull(storageClient, "Storage client must be provided");
        this.storageClient = storageClient;
    }

    @Override
    @Cacheable("listManResourceRelation")
    public PagedResponse<MainResourceRelation> list(Pageable pageable, Filterable filterable) {
        return storageClient.list(MAIN_RESOURCE_RELATION_VIEW_PATH, pageable, filterable, MainResourceRelation.class);
    }

    @Override
    @Cacheable("getMainResourceRelation")
    public MainResourceRelation get(Long id) {
        return storageClient.get(MAIN_RESOURCE_RELATION_VIEW_PATH, id, MainResourceRelation.class);
    }

    @Override
    @Cacheable("findMainResourceRelation")
    public List<MainResourceRelation> find(Filterable filterable) {
        return storageClient.find(MAIN_RESOURCE_RELATION_VIEW_PATH, filterable, MainResourceRelation.class);
    }

    @Override
    @CacheEvict(value = {"listManResourceRelation", "getMainResourceRelation", "findMainResourceRelation"}, allEntries = true)
    public List<Long> add(MainResourceRelation entity) {
        return storageClient.create(MAIN_RESOURCE_RELATION_PATH, entity);
    }

    @Override
    @CacheEvict(value = {"listManResourceRelation", "getMainResourceRelation", "findMainResourceRelation"}, allEntries = true)
    public void update(Long id, MainResourceRelation entity) {
        storageClient.update(MAIN_RESOURCE_RELATION_PATH, id, entity);
    }

    @Override
    @CacheEvict(value = {"listManResourceRelation", "getMainResourceRelation", "findMainResourceRelation"}, allEntries = true)
    public void remove(Long id) {
        storageClient.remove(MAIN_RESOURCE_RELATION_PATH, id);
    }

}
