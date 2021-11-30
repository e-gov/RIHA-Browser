package ee.ria.riha.logging.auditlog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.ria.riha.authentication.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

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
    public void log(AuditEvent auditEvent, AuditType auditType, HttpServletRequest request, Object... data) {
        StringBuilder sb = new StringBuilder();
        var userIdO = userContext.getRihaUserId();
        String userId = userIdO.isEmpty() ? "0" : userIdO.get();
        String userName = "";
        String organization = "";
        if (userIdO.isEmpty()) {
            userName = "ANON";
            organization = "ANON";
        } else {
            if (userContext.getRihaUserFullName().isPresent()) {
                userName = userContext.getRihaUserFullName().get();
            } else if (getActiveOrganization().isPresent()){
                organization = getActiveOrganization().get().toString();
            }
        }

        sb.append(String.format(" %s:%s; ", auditEvent, auditType));
        sb.append(String.format("%s:%s:%s:%s; ", userId, userName, organization, getClientIpAddress(request)));
        try {
            sb.append(mapper.writeValueAsString(data));
        } catch (JsonProcessingException e) {
            log.warn("Unable to serialize audit data to JSON");
        }
        log.info(sb.toString());
    }

    private static String getClientIpAddress(HttpServletRequest request) {
        String remoteAddress = "";
        if (request != null){
            remoteAddress = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddress == null || remoteAddress.equals("")){
                remoteAddress = request.getRemoteAddr();
            }
        }
        return remoteAddress;
    }
}
