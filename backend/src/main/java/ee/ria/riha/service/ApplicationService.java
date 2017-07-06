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
public class ApplicationService {

    @Autowired
    private ApplicationProperties applicationProperties;

    public Map<String, Object> getEnvironment() {
        HashMap<String, Object> environment = new HashMap<>();

        environment.put("remotes", applicationProperties.getRemoteApi());

        return environment;
    }
}
