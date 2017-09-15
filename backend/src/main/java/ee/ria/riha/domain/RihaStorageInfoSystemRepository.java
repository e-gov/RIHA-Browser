package ee.ria.riha.domain;

import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.service.ObjectNotFoundException;
import ee.ria.riha.storage.domain.MainResourceRepository;
import ee.ria.riha.storage.domain.model.MainResource;
import ee.ria.riha.storage.util.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Repository for InfoSystem entity persistence using RIHA-Storage.
 *
 * @author Valentin Suhnjov
 */
public class RihaStorageInfoSystemRepository implements InfoSystemRepository {

    private static final String NOT_IMPLEMENTED = "Not implemented";

    private static final Function<MainResource, InfoSystem> MAIN_RESOURCE_TO_INFO_SYSTEM = mainResource -> {
        if (mainResource == null) {
            return null;
        }
        return new InfoSystem(mainResource.getJson_context());
    };

    private static final Function<InfoSystem, MainResource> INFO_SYSTEM_TO_MAIN_RESOURCE = infoSystem -> {
        if (infoSystem == null) {
            return null;
        }
        return new MainResource(infoSystem.getJsonObject().toString());
    };

    private final MainResourceRepository mainResourceRepository;

    public RihaStorageInfoSystemRepository(MainResourceRepository mainResourceRepository) {
        this.mainResourceRepository = mainResourceRepository;
    }

    @Override
    public InfoSystem add(InfoSystem infoSystem) {
        List<Long> ids = mainResourceRepository.add(INFO_SYSTEM_TO_MAIN_RESOURCE.apply(infoSystem));

        MainResource mainResource = mainResourceRepository.get(ids.get(0));

        return MAIN_RESOURCE_TO_INFO_SYSTEM.apply(mainResource);
    }

    @Override
    public InfoSystem load(String shortName) {
        FilterRequest filter = new FilterRequest("short_name,=," + shortName, null, null);
        List<InfoSystem> infoSystems = find(filter);

        if (infoSystems.isEmpty()) {
            throw new ObjectNotFoundException("Could not resolve info system by short name " + shortName);
        }

        return infoSystems.get(0);
    }

    @Override
    public void update(String shortName, InfoSystem infoSystem) {
        throw new RuntimeException(NOT_IMPLEMENTED);
    }

    @Override
    public void remove(String shortName) {
        throw new RuntimeException(NOT_IMPLEMENTED);
    }

    @Override
    public PagedResponse<InfoSystem> list(Pageable pageable, Filterable filterable) {
        PagedResponse<MainResource> mainResourcePagedResponse = mainResourceRepository.list(pageable, filterable);

        return new PagedResponse<>(
                new PageRequest(mainResourcePagedResponse.getPage(), mainResourcePagedResponse.getSize()),
                mainResourcePagedResponse.getTotalElements(),
                mainResourcePagedResponse.getContent().stream()
                        .map(MAIN_RESOURCE_TO_INFO_SYSTEM)
                        .collect(Collectors.toList()));
    }

    @Override
    public List<InfoSystem> find(Filterable filterable) {
        List<MainResource> mainResources = mainResourceRepository.find(filterable);

        return mainResources.stream()
                .map(MAIN_RESOURCE_TO_INFO_SYSTEM)
                .collect(Collectors.toList());
    }
}
