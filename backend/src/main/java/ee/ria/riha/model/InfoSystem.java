package ee.ria.riha.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents information system. Holds information system data as JSON.
 *
 * @author Valentin Suhnjov
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class InfoSystem {

    @JsonRawValue
    @JsonProperty(value = "description")
    private String details;

    /**
     * Creates {@link ee.ria.riha.model.InfoSystem} with details data.
     *
     * @param details details
     */
    public InfoSystem(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }
}
