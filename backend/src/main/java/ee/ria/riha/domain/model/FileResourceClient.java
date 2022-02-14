package ee.ria.riha.domain.model;

import java.util.UUID;

/**
 * @author Valentin Suhnjov
 */
public class FileResourceClient {

    private UUID file_resource_uuid;
    private String infosystem_short_name;
    private String infosystem_owner_code;
    private String infosystem_owner_name;
    private UUID infosystem_uuid;
    private String file_resource_name;
    private String infosystem_name;

    public UUID getFile_resource_uuid() {
        return file_resource_uuid;
    }

    public void setFile_resource_uuid(UUID file_resource_uuid) {
        this.file_resource_uuid = file_resource_uuid;
    }

    public String getInfosystem_short_name() {
        return infosystem_short_name;
    }

    public void setInfosystem_short_name(String infosystem_short_name) {
        this.infosystem_short_name = infosystem_short_name;
    }

    public String getInfosystem_owner_code() {
        return infosystem_owner_code;
    }

    public void setInfosystem_owner_code(String infosystem_owner_code) {
        this.infosystem_owner_code = infosystem_owner_code;
    }

    public String getInfosystem_owner_name() {
        return infosystem_owner_name;
    }

    public void setInfosystem_owner_name(String infosystem_owner_name) {
        this.infosystem_owner_name = infosystem_owner_name;
    }

    public UUID getInfosystem_uuid() {
        return infosystem_uuid;
    }

    public void setInfosystem_uuid(UUID infosystem_uuid) {
        this.infosystem_uuid = infosystem_uuid;
    }

    public String getFile_resource_name() {
        return file_resource_name;
    }

    public void setFile_resource_name(String file_resource_name) {
        this.file_resource_name = file_resource_name;
    }

    public String getInfosystem_name() {
        return infosystem_name;
    }

    public void setInfosystem_name(String infosystem_name) {
        this.infosystem_name = infosystem_name;
    }
}
