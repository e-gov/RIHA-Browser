package ee.ria.riha.domain;

import ee.ria.riha.service.util.Filterable;
import ee.ria.riha.service.util.Pageable;
import ee.ria.riha.service.util.PagedResponse;

import java.util.List;

/**
 * Performs translation between RIHA-Storage resources and concrete entities
 *
 * @author Valentin Suhnjov
 */
public interface StorageRepository<K, T> {

    /**
     * List resources applying paging, filtering and sorting
     *
     * @param pageable   paging definition
     * @param filterable filter definition
     * @return paged and filtered list of resources
     */
    PagedResponse<T> list(Pageable pageable, Filterable filterable);

    /**
     * Retrieve resource by its id (primary key)
     *
     * @param id resource primary key
     * @return single resource
     */
    T get(K id);

    /**
     * Find resource applying filtering and sorting
     *
     * @param filterable filter definition
     * @return all found resources
     */
    List<T> find(Filterable filterable);

    /**
     * Creates resource
     *
     * @param entity resource model
     * @return list of created resource ids
     */
    List<K> add(T entity);

    /**
     * Updates resource
     *
     * @param id     id of a resource
     * @param entity model of updated resource
     */
    void update(K id, T entity);

    /**
     * Deletes resource
     *
     * @param id deleted resource id (primary key)
     */
    void remove(K id);
}
