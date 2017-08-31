package ee.ria.riha.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Valentin Suhnjov
 */
@Data
public class UserDetailsModel {

    private String personalCode;
    private String firstName;
    private String lastName;

    private List<String> roles = new ArrayList<>();

}
