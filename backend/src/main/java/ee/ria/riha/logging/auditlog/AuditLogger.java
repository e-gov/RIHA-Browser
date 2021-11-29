package ee.ria.riha.logging.auditlog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.ria.riha.authentication.RihaOrganization;
import ee.ria.riha.authentication.UserContext;
import ee.ria.riha.service.IllegalBrowserStateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static ee.ria.riha.service.SecurityContextUtil.getActiveOrganization;


@Slf4j
@Component
public class AuditLogger {

    final UserContext userContext;
    final ObjectMapper mapper = new ObjectMapper();

    public AuditLogger(UserContext userContext) {
        this.userContext = userContext;
    }

    /**
     * Auditlog.
     * data objects will be serialized to JSON.
     */
    public void log(AuditEvent auditEvent, AuditType auditType, Object... data) {
        StringBuilder sb = new StringBuilder();
        var userIdO = userContext.getRihaUserId();
        String userId = userIdO.isEmpty() ? "0" : userIdO.get();
        String userName = "";
        if (userIdO.isEmpty()) {
            userName = "ANON";
        } else {
            if (userContext.getRihaUserFullName().isPresent()) {
                userName = userContext.getRihaUserFullName().get();
            }
        }
        RihaOrganization organization = getActiveOrganization()
                .orElseThrow(() -> new IllegalBrowserStateException("Unable to retrieve active organization"));

        sb.append(String.format(" %s:%s; ", auditEvent, auditType));
        sb.append(String.format("%s:%s:%s; ", userId, userName, organization));
        try {
            sb.append(mapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            log.warn("Unable to serialize audit data to JSON");
        }
        log.info(sb.toString());
    }
}
