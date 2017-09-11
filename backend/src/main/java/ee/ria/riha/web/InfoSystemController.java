package ee.ria.riha.web;

import ee.ria.riha.model.InfoSystem;
import ee.ria.riha.service.InfoSystemService;
import ee.ria.riha.storage.util.Filterable;
import ee.ria.riha.storage.util.Pageable;
import ee.ria.riha.storage.util.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;

@RestController
@RequestMapping(API_V1_PREFIX + "/systems")
public class InfoSystemController {

    @Autowired
    private InfoSystemService infoSystemService;

    @GetMapping
    public PagedResponse<InfoSystem> list(Pageable pageable, Filterable filterable) {
        return infoSystemService.list(pageable, filterable);
    }

    @GetMapping("/{infoSystemUuid}")
    public InfoSystem getByUuid(@PathVariable("infoSystemUuid") UUID uuid) {
        return infoSystemService.findByUuid(uuid);
    }

}
