package ee.ria.riha.authentication;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class OrganizationRoleMapping {

    private String name;
    private String code;
    private GrantedAuthority authority;
}
