package ee.ria.riha.service;

import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.domain.model.Relation;
import ee.ria.riha.domain.model.RelationType;
import ee.ria.riha.storage.domain.MainResourceRelationRepository;
import ee.ria.riha.storage.domain.model.MainResourceRelation;
import ee.ria.riha.storage.util.FilterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Valentin Suhnjov
 */
@Service
public class RelationService {

    private static final Function<MainResourceRelation, Relation> MAIN_RESOURCE_RELATION_TO_RELATION = mainResourceRelation -> {
        if (mainResourceRelation == null) {
            return null;
        }

        return Relation.builder()
                .id(mainResourceRelation.getMain_resource_relation_id())
                .infoSystemUuid(mainResourceRelation.getInfosystem_uuid())
                .infoSystemName(mainResourceRelation.getInfosystem_name())
                .infoSystemShortName(mainResourceRelation.getInfosystem_short_name())
                .relatedInfoSystemUuid(mainResourceRelation.getRelated_infosystem_uuid())
                .relatedInfoSystemName(mainResourceRelation.getRelated_infosystem_name())
                .relatedInfoSystemShortName(mainResourceRelation.getRelated_infosystem_short_name())
                .type(mainResourceRelation.getType() != null
                        ? RelationType.valueOf(mainResourceRelation.getType())
                        : null)
                .creationDate(mainResourceRelation.getCreation_date())
                .modifiedDate(mainResourceRelation.getModified_date())
                .build();
    };

    private MainResourceRelationRepository mainResourceRelationRepository;

    private InfoSystemService infoSystemService;

    /**
     * Lists {@link Relation}s for info system with given short name. List consists of two types of {@link Relation}s:
     * <ul><li>In direct relation, info system with given short name is related <strong>to</strong> another info system.
     * </li><li>In reverse relation, info system with given short name is related <strong>by</strong> another system.
     * Such {@link Relation} is marked as reversed.</li></ul>
     *
     * @param shortName info system short name
     * @return list of relations (related info systems)
     */
    public List<Relation> listRelations(String shortName) {
        InfoSystem infoSystem = infoSystemService.get(shortName);

        List<MainResourceRelation> directRelations = getDirectRelations(infoSystem);

        List<Relation> allRelations = directRelations.stream()
                .map(MAIN_RESOURCE_RELATION_TO_RELATION)
                .collect(Collectors.toList());

        List<MainResourceRelation> reverseRelations = getReverseRelations(infoSystem);

        allRelations.addAll(reverseRelations.stream()
                .map(MAIN_RESOURCE_RELATION_TO_RELATION)
                .peek(relation -> relation.setReversed(true))
                .collect(Collectors.toList())
        );

        return allRelations;
    }

    private List<MainResourceRelation> getReverseRelations(InfoSystem infoSystem) {
        return mainResourceRelationRepository.find(
                new FilterRequest("related_infosystem_uuid,=," + infoSystem.getUuid(), null, null));
    }

    private List<MainResourceRelation> getDirectRelations(InfoSystem infoSystem) {
        return mainResourceRelationRepository.find(
                new FilterRequest("infosystem_uuid,=," + infoSystem.getUuid(), null, null));
    }

    @Autowired
    public void setMainResourceRelationRepository(
            MainResourceRelationRepository mainResourceRelationRepository) {
        this.mainResourceRelationRepository = mainResourceRelationRepository;
    }

    @Autowired
    public void setInfoSystemService(InfoSystemService infoSystemService) {
        this.infoSystemService = infoSystemService;
    }
}
