package ee.ria.riha.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoSystemDocumentMetadata extends InfoSystemFileMetadata {

    private boolean accessRestricted;
}
