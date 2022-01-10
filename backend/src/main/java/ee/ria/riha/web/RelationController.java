package ee.ria.riha.web;

import ee.ria.riha.domain.model.Relation;
import ee.ria.riha.domain.model.RelationResponse;
import ee.ria.riha.service.RelationService;
import ee.ria.riha.service.auth.PreAuthorizeInfoSystemOwner;
import ee.ria.riha.web.model.RelationModel;
import ee.ria.riha.web.model.RelationResponseSummaryModel;
import ee.ria.riha.web.model.RelationSummaryModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;

/**
 * Controller for info system relationship management.
 *
 * @author Valentin Suhnjov
 */
@RestController
@RequestMapping(API_V1_PREFIX + "/systems")
@Api("Information system relations")
public class RelationController {

    private static final Function<Relation, RelationSummaryModel> RELATION_TO_RELATION_SUMMARY_MODEL = relation -> {
        if (relation == null) {
            return null;
        }

        Relation normalizedRelation = relation.isReversed() ? relation.reverse() : relation;

        return RelationSummaryModel.builder()
                .id(normalizedRelation.getId())
                .infoSystemUuid(normalizedRelation.getRelatedInfoSystemUuid())
                .infoSystemName(normalizedRelation.getRelatedInfoSystemName())
                .infoSystemShortName(normalizedRelation.getRelatedInfoSystemShortName())
                .type(normalizedRelation.getType())
                .build();
    };

    private static final Function<RelationResponse, RelationResponseSummaryModel> RELATION_TO_RELATIONRESPONSE_SUMMARY_MODEL = relation -> {
        if (relation == null) {
            return null;
        }

        RelationResponse normalizedRelation = relation.isReversed() ? relation.reverse() : relation;

        return RelationResponseSummaryModel.builder()
                .id(normalizedRelation.getId())
                .infoSystemUuid(normalizedRelation.getRelatedInfoSystemUuid())
                .infoSystemName(normalizedRelation.getRelatedInfoSystemName())
                .infoSystemShortName(normalizedRelation.getRelatedInfoSystemShortName())
                .infoSystemStatus(normalizedRelation.getInfoSystemStatus())
                .type(normalizedRelation.getType())
                .build();
    };

    private RelationService relationService;

    private RelationSummaryModel createModel(Relation relation) {
        return RELATION_TO_RELATION_SUMMARY_MODEL.apply(relation);
    }

    @GetMapping("/{reference}/relations")
    @ApiOperation("List all info system relations")
    public ResponseEntity<List<RelationResponseSummaryModel>> list(@PathVariable("reference") String reference) {
        List<RelationResponse> relations = relationService.listRelations(reference);
        return ResponseEntity.ok(createModel(relations));
    }

    private List<RelationResponseSummaryModel> createModel(List<RelationResponse> relations) {
        return relations.stream()
                .map(RELATION_TO_RELATIONRESPONSE_SUMMARY_MODEL)
                .collect(Collectors.toList());
    }

    @PostMapping("/{reference}/relations")
    @PreAuthorizeInfoSystemOwner
    @ApiOperation("Create new relation for information system")
    public ResponseEntity<RelationSummaryModel> add(@PathVariable("reference") String reference,
                                                    @RequestBody RelationModel relationModel) {
        Relation createdRelation = relationService.createRelation(reference, relationModel);
        return ResponseEntity.ok(createModel(createdRelation));
    }

    @DeleteMapping("/{reference}/relations/{relationId}")
    @PreAuthorizeInfoSystemOwner
    @ApiOperation("Deletes single relation of information system")
    public void delete(@PathVariable("reference") String reference, @PathVariable("relationId") Long relationId) {
        relationService.delete(reference, relationId);
    }

    @Autowired
    public void setRelationService(RelationService relationService) {
        this.relationService = relationService;
    }
}
