package ee.ria.riha.service;

import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.domain.model.Relation;
import ee.ria.riha.domain.model.RelationType;
import ee.ria.riha.storage.domain.MainResourceRelationRepository;
import ee.ria.riha.storage.domain.model.MainResourceRelation;
import ee.ria.riha.storage.util.FilterRequest;
import ee.ria.riha.web.model.RelationModel;
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

    private static final Function<Relation, MainResourceRelation> RELATION_TO_MAIN_RESOURCE_RELATION = relation -> {
        if (relation == null) {
            return null;
        }

        MainResourceRelation mainResourceRelation = new MainResourceRelation();
        mainResourceRelation.setMain_resource_relation_id(relation.getId());
        mainResourceRelation.setInfosystem_uuid(relation.getInfoSystemUuid());
        mainResourceRelation.setInfosystem_name(relation.getInfoSystemName());
        mainResourceRelation.setInfosystem_short_name(relation.getInfoSystemShortName());
        mainResourceRelation.setRelated_infosystem_uuid(relation.getRelatedInfoSystemUuid());
        mainResourceRelation.setRelated_infosystem_name(relation.getRelatedInfoSystemName());
        mainResourceRelation.setRelated_infosystem_short_name(relation.getRelatedInfoSystemShortName());
        mainResourceRelation.setType(relation.getType().name());
        mainResourceRelation.setCreation_date(relation.getCreationDate());
        mainResourceRelation.setModified_date(relation.getModifiedDate());

        return mainResourceRelation;
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

    /**
     * Creates {@link Relation} from info system with given short name to another info system with given type.
     *
     * @param shortName    source info system short name
     * @param relatedModel related info system model
     * @return created {@link Relation}
     */
    public Relation createRelation(String shortName, RelationModel relatedModel) {
        Relation relation = createRelationFromModel(shortName, relatedModel);

        Relation normalizedRelation = normalizeRelation(relation);

        List<Long> createRelationIds = mainResourceRelationRepository.add(
                RELATION_TO_MAIN_RESOURCE_RELATION.apply(normalizedRelation));

        if (createRelationIds.isEmpty()) {
            throw new IllegalBrowserStateException("Relation was not created");
        }

        Relation createdRelation = MAIN_RESOURCE_RELATION_TO_RELATION.apply(
                mainResourceRelationRepository.get(createRelationIds.get(0)));
        createdRelation.setReversed(normalizedRelation.isReversed());

        return createdRelation;
    }

    private Relation createRelationFromModel(String shortName, RelationModel model) {
        InfoSystem infoSystem = infoSystemService.get(shortName);
        InfoSystem relatedInfoSystem = infoSystemService.get(model.getInfoSystemShortName());

        Relation relation = new Relation();
        relation.setInfoSystemUuid(infoSystem.getUuid());
        relation.setRelatedInfoSystemUuid(relatedInfoSystem.getUuid());
        relation.setType(model.getType());
        return relation;
    }

    /**
     * Normalizes {@link Relation} by choosing minimal ordinal value of {@link RelationType} or its opposite type. In
     * case opposite value has lower ordinal value, {@link Relation} is reversed.
     *
     * @param relation relation to normalize
     * @return normalized relation
     */
    private Relation normalizeRelation(Relation relation) {
        if (relation.getType().ordinal() < relation.getType().getOpposite().ordinal()) {
            return relation;
        }

        return relation.reverse();
    }

    /**
     * Deletes single {@link Relation} if info system with given short name is part of this relation.
     *
     * @param shortName  info system short name that is part of relation
     * @param relationId id of a relation
     */
    public void delete(String shortName, Long relationId) {
        InfoSystem infoSystem = infoSystemService.get(shortName);

        MainResourceRelation mainResourceRelation = mainResourceRelationRepository.get(relationId);

        if (!mainResourceRelation.getInfosystem_uuid().equals(infoSystem.getUuid())
                && !mainResourceRelation.getRelated_infosystem_uuid().equals(infoSystem.getUuid())) {
            throw new IllegalStateException("Info system is not part of deleted relation");
        }

        mainResourceRelationRepository.remove(relationId);
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
