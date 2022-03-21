package ee.ria.riha.authentication;

import org.springframework.stereotype.Component;

import java.util.Optional;

import static ee.ria.riha.service.SecurityContextUtil.getRihaUserDetails;

@Component
public class UserContext {

    public Optional<String> getRihaUserId() {
        return getRihaUserDetails().map(RihaUserDetails::getPersonalCode);
    }

    public Optional<String> getRihaUserFullName() {
        return getRihaUserDetails().map(RihaUserDetails::getFullName);
    }
}
