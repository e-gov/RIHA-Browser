package ee.ria.riha.logging.auditlog;

import ee.ria.riha.authentication.UserContext;
import ee.ria.riha.domain.model.InfoSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.WARN)
@ExtendWith(MockitoExtension.class)
public class AuditLoggerTest {

    @Mock
    private UserContext userContext;

    @InjectMocks
    private AuditLogger auditLogger;

    @Mock
    HttpServletRequest request;

    @Test
    public void withUserId() {
        when(userContext.getRihaUserId()).thenReturn(Optional.of("1"));
        when(userContext.getRihaUserFullName()).thenReturn(Optional.of("test"));
        auditLogger.log(AuditEvent.CREATE, AuditType.INFOSYSTEM, request, new InfoSystem());
    }

    @Test
    public void withoutUserId() {
        auditLogger.log(AuditEvent.CREATE, AuditType.INFOSYSTEM, request, new InfoSystem());
    }

    @Test
    public void withoutOptName() {
        when(userContext.getRihaUserId()).thenReturn(Optional.of("1"));
        auditLogger.log(AuditEvent.CREATE, AuditType.INFOSYSTEM, request, new InfoSystem());
    }

    @Test
    public void withOrganization() {
        auditLogger.log(AuditEvent.CREATE, AuditType.INFOSYSTEM, request, new InfoSystem());
    }
}