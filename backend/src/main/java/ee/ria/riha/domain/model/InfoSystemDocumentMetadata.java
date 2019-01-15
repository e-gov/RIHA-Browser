package ee.ria.riha.domain.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * Represents info system document metadata.
 */
@Data
public class InfoSystemDocumentMetadata extends InfoSystemFileMetadata {

    private boolean accessRestricted;

    private JsonNode accessRestrictionJson;

    public boolean wasChanged(InfoSystemDocumentMetadata prevVersion) {
        return super.wasChanged(prevVersion)
                || !this.accessRestrictionJson.toString().equals(prevVersion.getAccessRestrictionJson().toString());
    }
}