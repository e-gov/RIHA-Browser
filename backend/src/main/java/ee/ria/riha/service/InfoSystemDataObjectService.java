package ee.ria.riha.service;

import ee.ria.riha.domain.InfoSystemDataObjectRepository;
import ee.ria.riha.domain.model.InfoSystemDataObject;
import ee.ria.riha.service.util.Filterable;
import ee.ria.riha.service.util.Pageable;
import ee.ria.riha.service.util.PagedResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InfoSystemDataObjectService {

    @Autowired
    private InfoSystemDataObjectRepository infoSystemDataObjectRepository;


    public PagedResponse<InfoSystemDataObject> list(Pageable pageable, Filterable filterable) {
        return infoSystemDataObjectRepository.list(pageable, filterable);
    }
}
