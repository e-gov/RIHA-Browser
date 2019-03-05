package ee.ria.riha.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Valentin Suhnjov
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsModel {

    private String personalCode;
    private String firstName;
    private String lastName;
    private String email;

    private Boolean approver;
    private Boolean producer;

    private List<OrganizationModel> organizations = new ArrayList<>();
    private OrganizationModel activeOrganization;

    private List<String> roles = new ArrayList<>();

}
