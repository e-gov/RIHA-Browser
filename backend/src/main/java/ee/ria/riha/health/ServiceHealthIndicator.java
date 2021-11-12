package ee.ria.riha.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class ServiceHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        Health.Builder status = Health.up();
        Map<String, Object> details = new HashMap<>();
        details.put("appName", "test");
        details.put("version", "test");
        details.put("packagingTime", LocalDateTime.now());
        details.put("appStartTime", LocalDateTime.now());
        details.put("serverTime", LocalDateTime.now());

        return status.withDetails(details).build();
    }
}
