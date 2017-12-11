package ee.ria.riha.web;

import ee.ria.riha.domain.model.Issue;
import ee.ria.riha.web.model.IssueSummaryModel;
import org.springframework.stereotype.Component;

/**
 * Maps {@link Issue} to {@link IssueSummaryModel}.
 */
@Component
public class IssueSummaryModelMapper implements ModelMapper<Issue, IssueSummaryModel> {

    /**
     * Maps {@link Issue} entity to {@link IssueSummaryModel}.
     *
     * @param value entity
     * @return issue model
     */
    @Override
    public IssueSummaryModel map(Issue value) {
        IssueSummaryModel model = new IssueSummaryModel();

        model.setId(value.getId());
        model.setInfoSystemUuid(value.getInfoSystemUuid());
        model.setDateCreated(value.getDateCreated());
        model.setTitle(value.getTitle());
        model.setStatus(value.getStatus());
        model.setOrganizationCode(value.getOrganizationCode());
        model.setOrganizationName(value.getOrganizationName());
        model.setType(value.getType());

        return model;
    }

}
