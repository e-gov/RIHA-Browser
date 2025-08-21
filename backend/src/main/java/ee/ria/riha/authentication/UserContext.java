package ee.ria.riha.authentication;

import static ee.ria.riha.service.SecurityContextUtil.getRihaUserDetails;

import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class UserContext {

  public Optional<String> getRihaUserId() {
    return getRihaUserDetails().map(RihaUserDetails::getPersonalCode);
  }

  public Optional<String> getRihaUserFullName() {
    return getRihaUserDetails().map(RihaUserDetails::getFullName);
  }
}
