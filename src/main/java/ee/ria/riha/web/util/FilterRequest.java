package ee.ria.riha.web.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Valentin Suhnjov
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FilterRequest implements Filterable{

    private String filter;
    private String sort;
    private String fields;

}
