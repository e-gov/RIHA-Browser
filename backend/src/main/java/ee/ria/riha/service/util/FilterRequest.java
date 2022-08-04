package ee.ria.riha.service.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Valentin Suhnjov
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilterRequest implements Filterable, Serializable {

    private String filter;
    private String sort;
    private String fields;

    public FilterRequest addFilter(String filter) {
        this.filter = this.filter == null ? filter : this.filter + "," + filter;
        return this;
    }

}
