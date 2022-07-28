package ee.ria.riha.domain.model;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;
import java.util.Date;

/**
 * Model for Main_resource (Information System) entity.
 *
 * @author Valentin Suhnjov
 */
public class MainResource implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long main_resource_id;

    @JsonRawValue
    private JsonNode json_content;

    private String last_positive_approval_request_type;
    private Date last_positive_approval_request_date;
    private Date last_positive_establishment_request_date;
    private Date last_positive_take_into_use_request_date;
    private Date last_positive_finalization_request_date;
    private boolean hasUsedSystemTypeRelations;

    public Long getMain_resource_id() {
        return main_resource_id;
    }

    public void setMain_resource_id(Long main_resource_id) {
        this.main_resource_id = main_resource_id;
    }

    public JsonNode getJson_content() {
        return json_content;
    }

    public void setJson_content(JsonNode json_content) {
        this.json_content = json_content;
    }

    public String getLast_positive_approval_request_type() {
        return last_positive_approval_request_type;
    }

    public void setLast_positive_approval_request_type(String last_positive_approval_request_type) {
        this.last_positive_approval_request_type = last_positive_approval_request_type;
    }

    public Date getLast_positive_approval_request_date() {
        return last_positive_approval_request_date;
    }

    public void setLast_positive_approval_request_date(Date last_positive_approval_request_date) {
        this.last_positive_approval_request_date = last_positive_approval_request_date;
    }

    public Date getLast_positive_establishment_request_date() {
        return last_positive_establishment_request_date;
    }

    public void setLast_positive_establishment_request_date(Date last_positive_establishment_request_date) {
        this.last_positive_establishment_request_date = last_positive_establishment_request_date;
    }

    public Date getLast_positive_take_into_use_request_date() {
        return last_positive_take_into_use_request_date;
    }

    public void setLast_positive_take_into_use_request_date(Date last_positive_take_into_use_request_date) {
        this.last_positive_take_into_use_request_date = last_positive_take_into_use_request_date;
    }

    public Date getLast_positive_finalization_request_date() {
        return last_positive_finalization_request_date;
    }

    public void setLast_positive_finalization_request_date(Date last_positive_finalization_request_date) {
        this.last_positive_finalization_request_date = last_positive_finalization_request_date;
    }

    public boolean isHasUsedSystemTypeRelations() {
        return hasUsedSystemTypeRelations;
    }

    public void setHasUsedSystemTypeRelations(boolean hasUsedSystemTypeRelations) {
        this.hasUsedSystemTypeRelations = hasUsedSystemTypeRelations;
    }
}
