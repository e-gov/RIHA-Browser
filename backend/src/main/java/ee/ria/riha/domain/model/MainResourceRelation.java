package ee.ria.riha.domain.model;

import java.util.Date;
import java.util.UUID;

/**
 * Model for {@link MainResource} relationship information.
 *
 * @author Valentin Suhnjov
 */
public class MainResourceRelation {

    private Long main_resource_relation_id;
    private Date creation_date;
    private Date modified_date;
    private UUID infosystem_uuid;
    private String infosystem_name;
    private String infosystem_short_name;
    private UUID related_infosystem_uuid;
    private String related_infosystem_name;
    private String related_infosystem_short_name;
    private String type;
    private String infosystem_status;
    private String related_infosystem_status;

    public String getRelated_infosystem_status() {
        return related_infosystem_status;
    }

    public void setRelated_infosystem_status(String related_infosystem_status) {
        this.related_infosystem_status = related_infosystem_status;
    }

    public String getInfosystem_status() {
        return infosystem_status;
    }

    public void setInfosystem_status(String infosystem_status) {
        this.infosystem_status = infosystem_status;
    }

    public Long getMain_resource_relation_id() {
        return main_resource_relation_id;
    }

    public void setMain_resource_relation_id(Long main_resource_relation_id) {
        this.main_resource_relation_id = main_resource_relation_id;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public Date getModified_date() {
        return modified_date;
    }

    public void setModified_date(Date modified_date) {
        this.modified_date = modified_date;
    }

    public UUID getInfosystem_uuid() {
        return infosystem_uuid;
    }

    public void setInfosystem_uuid(UUID infosystem_uuid) {
        this.infosystem_uuid = infosystem_uuid;
    }

    public UUID getRelated_infosystem_uuid() {
        return related_infosystem_uuid;
    }

    public void setRelated_infosystem_uuid(UUID related_infosystem_uuid) {
        this.related_infosystem_uuid = related_infosystem_uuid;
    }

    public String getInfosystem_name() {
        return infosystem_name;
    }

    public void setInfosystem_name(String infosystem_name) {
        this.infosystem_name = infosystem_name;
    }

    public String getInfosystem_short_name() {
        return infosystem_short_name;
    }

    public void setInfosystem_short_name(String infosystem_short_name) {
        this.infosystem_short_name = infosystem_short_name;
    }

    public String getRelated_infosystem_name() {
        return related_infosystem_name;
    }

    public void setRelated_infosystem_name(String related_infosystem_name) {
        this.related_infosystem_name = related_infosystem_name;
    }

    public String getRelated_infosystem_short_name() {
        return related_infosystem_short_name;
    }

    public void setRelated_infosystem_short_name(String related_infosystem_short_name) {
        this.related_infosystem_short_name = related_infosystem_short_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
