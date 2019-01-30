package ee.ria.riha.domain.model;

import lombok.Data;

/**
 * Represents basic info system file metadata.
 *
 * @author Valentin Suhnjov
 */
@Data
public class InfoSystemFileMetadata {

    private String name;
    private String url;
    private String type;
    private String creationTimestamp;
    private String updateTimestamp;

    public boolean wasChanged(InfoSystemFileMetadata prevVersion) {
        return !this.name.equals(prevVersion.name) || !this.url.equals(prevVersion.url);
    }

}
