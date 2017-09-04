package ee.ria.riha.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Valentin Suhnjov
 */
@Data
public class OrganizationModel {

    private String code;
    private String name;
    private List<String> roles = new ArrayList<>();

}
