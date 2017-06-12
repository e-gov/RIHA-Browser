package ee.ria.riha.web;

import ee.ria.riha.service.InfoSystemService;
import ee.ria.riha.web.util.Filterable;
import ee.ria.riha.web.util.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/systems")
public class InfoSystemController {

    @Autowired
    private InfoSystemService infoSystemService;

    @GetMapping
    public Object list(Pageable pageable, Filterable filterable) {
        return infoSystemService.list(pageable, filterable);
    }

}
