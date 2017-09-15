package ee.ria.riha.service;

import ee.ria.riha.conf.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Valentin Suhnjov
 */
@Service
public class EnvironmentService {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private UserService userService;

    public Map<String, Object> getEnvironment() {
        HashMap<String, Object> environment = new HashMap<>();

        environment.put("remotes", applicationProperties.getRemoteApi());
        environment.put("userDetails", userService.getUserDetails());

        return environment;
    }

    /**
     * @deprecated use user service instead
     */
    @Deprecated
    public void changeActiveOrganization(String organizationCode) {
        userService.changeActiveOrganization(organizationCode);
    }

}
