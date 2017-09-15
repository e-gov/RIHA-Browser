package ee.ria.riha.domain;

import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.storage.util.Filterable;
import ee.ria.riha.storage.util.Pageable;
import ee.ria.riha.storage.util.PagedResponse;

import java.util.List;

/**
 * Interface for repositories that persist InfoSystem entities.
 *
 * @author Valentin Suhnjov
 */
public interface InfoSystemRepository {

    InfoSystem add(InfoSystem infoSystem);

    InfoSystem load(String shortName);

    void update(String shortName, InfoSystem infoSystem);

    void remove(String shortName);

    PagedResponse<InfoSystem> list(Pageable pageable, Filterable filterable);

    List<InfoSystem> find(Filterable filterable);
}
