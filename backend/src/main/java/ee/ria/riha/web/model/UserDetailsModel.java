package ee.ria.riha.web.model;

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

    private List<OrganizationModel> organizations = new ArrayList<>();
    private OrganizationModel activeOrganization;

    private List<String> roles = new ArrayList<>();

}
