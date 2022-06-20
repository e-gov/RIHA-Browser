package ee.ria.riha.logging.auditlog;

import ee.ria.riha.authentication.UserContext;
import ee.ria.riha.domain.model.InfoSystem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
}