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

    @Override
    public boolean wasChanged(InfoSystemFileMetadata prevVersion) {
        if (prevVersion instanceof InfoSystemDocumentMetadata) {
            return super.wasChanged(prevVersion)
                    || accessRestrictionWasChanged(
                    ((InfoSystemDocumentMetadata) prevVersion).getAccessRestrictionJson(),
                    this.getAccessRestrictionJson());
        } else {
            return super.wasChanged(prevVersion);
        }
    }

    private boolean accessRestrictionWasChanged(JsonNode oldAccessRestriction, JsonNode newAccessRestriction) {
        if (oldAccessRestriction == null && newAccessRestriction == null) {
            return false;
        } else if (newAccessRestriction != null && oldAccessRestriction != null) {
            return newAccessRestriction.toString().equals(oldAccessRestriction.toString());
        } else {
            return true;
        }
    }
}