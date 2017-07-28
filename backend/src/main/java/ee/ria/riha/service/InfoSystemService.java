package ee.ria.riha.service;

import ee.ria.riha.model.InfoSystem;
import ee.ria.riha.storage.domain.MainResourceRepository;
import ee.ria.riha.storage.domain.model.MainResource;
import ee.ria.riha.storage.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Valentin Suhnjov
 */
@Service
public class InfoSystemService {

    private static final Function<MainResource, InfoSystem> MAIN_RESOURCE_TO_INFO_SYSTEM = mainResource -> new InfoSystem(
            mainResource.getJson_context());

    @Autowired
    private MainResourceRepository mainResourceRepository;

    public PagedResponse<InfoSystem> list(Pageable pageable, Filterable filterable) {
        PagedResponse<MainResource> pagedResponse = mainResourceRepository.list(pageable, filterable);

        return new PagedResponse<>(new PageRequest(pagedResponse.getPage(), pagedResponse.getSize()),
                                   pagedResponse.getTotalElements(),
                                   pagedResponse.getContent().stream()
                                           .map(MAIN_RESOURCE_TO_INFO_SYSTEM)
                                           .collect(Collectors.toList())
        );
    }

    public InfoSystem findByUuid(UUID uuid) {
        if (uuid == null) {
            return null;
        }

        FilterRequest filter = new FilterRequest("uuid,=," + uuid.toString(), null, null);
        List<MainResource> mainResources = mainResourceRepository.find(filter);

        if (mainResources.isEmpty()) {
            return null;
        }

        return MAIN_RESOURCE_TO_INFO_SYSTEM.apply(mainResources.get(0));
    }

}
