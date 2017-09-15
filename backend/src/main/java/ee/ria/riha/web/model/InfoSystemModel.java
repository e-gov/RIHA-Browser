package ee.ria.riha.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Valentin Suhnjov
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class InfoSystemModel {

    private Integer id;

    @JsonIgnore
    private String json;

    @JsonRawValue
    public String getDetails() {
        return json;
    }

    @JsonSetter
    public void setDetails(JsonNode raw) {
        this.json = raw.toString();
    }

    public String getJson() {
        return this.json;
    }

    public void setJson(String description) {
        this.json = description;
    }
}
