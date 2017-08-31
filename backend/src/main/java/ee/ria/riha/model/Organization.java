package ee.ria.riha.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Valentin Suhnjov
 */
@EqualsAndHashCode(of = "code")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Organization {

    private String code;
    private String name;

}
