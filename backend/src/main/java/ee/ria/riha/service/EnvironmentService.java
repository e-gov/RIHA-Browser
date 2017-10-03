package ee.ria.riha.service;

import ee.ria.riha.conf.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Valentin Suhnjov
 */
@Service
public class EnvironmentService {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private UserService userService;

    /**
     * @deprecated use user service instead
     */
    @Deprecated
    public void changeActiveOrganization(String organizationCode) {
        userService.changeActiveOrganization(organizationCode);
    }

}
