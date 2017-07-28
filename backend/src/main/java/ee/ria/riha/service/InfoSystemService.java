package ee.ria.riha.service;

import ee.ria.riha.model.InfoSystem;
import ee.ria.riha.storage.domain.MainResourceRepository;
import ee.ria.riha.storage.domain.model.MainResource;
import ee.ria.riha.storage.util.Filterable;
import ee.ria.riha.storage.util.PageRequest;
import ee.ria.riha.storage.util.Pageable;
import ee.ria.riha.storage.util.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        PagedResponse<MainResource> response = mainResourceRepository.list(pageable, filterable);

        return new PagedResponse<>(new PageRequest(response.getPage(), response.getSize()),
                                   response.getTotalElements(),
                                   response.getContent().stream()
                                           .map(MAIN_RESOURCE_TO_INFO_SYSTEM)
                                           .collect(Collectors.toList())
        );
    }

}
