package ee.ria.riha.domain.model;

import lombok.Data;

/**
 * Represents info system document metadata.
 */
@Data
public class InfoSystemDocumentMetadata extends InfoSystemFileMetadata {

    private boolean accessRestricted;

}
