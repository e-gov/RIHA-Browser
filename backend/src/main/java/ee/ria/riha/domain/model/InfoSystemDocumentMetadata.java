package ee.ria.riha.domain.model;

import lombok.Builder;
import lombok.Data;

/**
 * Represents
 * @author Valentin Suhnjov
 */
@Data
@Builder
public class InfoSystemDocumentMetadata {

    private String name;
    private String url;
    private boolean accessRestricted;

}
