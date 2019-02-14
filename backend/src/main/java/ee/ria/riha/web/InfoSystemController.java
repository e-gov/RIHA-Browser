package ee.ria.riha.web;

import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.service.InfoSystemDataObjectService;
import ee.ria.riha.service.InfoSystemService;
import ee.ria.riha.service.auth.PreAuthorizeInfoSystemOwner;
import ee.ria.riha.service.auth.PrincipalHasRoleProducer;
import ee.ria.riha.storage.util.*;
import ee.ria.riha.web.model.InfoSystemModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(API_V1_PREFIX + "/systems")
@Api("Information systems")
public class InfoSystemController {

    @Autowired
    private InfoSystemService infoSystemService;

    @Autowired
    private InfoSystemDataObjectService infoSystemDataObjectService;

    @Autowired
    private InfoSystemModelMapper infoSystemModelMapper;

    @Autowired
    private InfoSystemDataObjectMapper infoSystemDataObjectMapper;

    @GetMapping
    @ApiOperation("List all existing information systems")
    @ApiPageableAndFilterableParams
    public ResponseEntity list(Pageable pageable, Filterable filterable) {
        return ResponseEntity.ok(
                createPagedModel(
                        infoSystemService.list(pageable, filterable),
                        infoSystemModelMapper));
    }

    @GetMapping(path = "/data-objects")
    @ApiOperation("List all existing information systems data objects")
    @ApiPageableAndFilterableParams
    public ResponseEntity listDataObjects(Pageable pageable, Filterable filterable) {
        return ResponseEntity.ok(
                createPagedModel(
                        infoSystemDataObjectService.list(pageable, filterable),
                        infoSystemDataObjectMapper));
    }

    private  <E,R> PagedResponse<R> createPagedModel(PagedResponse<E> list, ModelMapper<E,R> mapper) {
        return new PagedResponse<>(new PageRequest(list.getPage(), list.getSize()),
                                   list.getTotalElements(),
                                   list.getContent().stream()
                                           .map(mapper::map)
                                           .collect(toList()));
    }

    @PostMapping
    @ApiOperation("Create new information system")
    @PrincipalHasRoleProducer
    public ResponseEntity<InfoSystemModel> create(@RequestBody InfoSystemModel model) {
        InfoSystem infoSystem = infoSystemService.create(new InfoSystem(model.getJson()));
        return ResponseEntity.ok(infoSystemModelMapper.map(infoSystem));
    }

    @GetMapping("/{reference}")
    @ApiOperation("Get existing information system")
    public ResponseEntity<InfoSystemModel> get(@PathVariable("reference") String reference) {
        InfoSystem infoSystem = infoSystemService.get(reference);
        return ResponseEntity.ok(infoSystemModelMapper.map(infoSystem));
    }

    @PutMapping("/{reference}")
    @PreAuthorizeInfoSystemOwner
    @ApiOperation("Update existing information system")
    public ResponseEntity<InfoSystemModel> update(@PathVariable("reference") String reference,
                                                  @RequestBody InfoSystemModel model) {
        InfoSystem infoSystem = infoSystemService.update(reference, new InfoSystem(model.getJson()));
        return ResponseEntity.ok(infoSystemModelMapper.map(infoSystem));
    }

}
