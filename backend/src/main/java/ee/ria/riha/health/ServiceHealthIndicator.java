package ee.ria.riha.health;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.PropertySource;

import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@PropertySource("classpath:heartbeat.properties")
public class ServiceHealthIndicator implements HealthIndicator {

    @Value("${app.name}")
    private String appName;
    @Value("${app.version}")
    private String appVersion;
    @Value("${app.packaging.time}")
    private String appPackagingTime;

    RuntimeMXBean runtimeBeen = ManagementFactory.getRuntimeMXBean();
    Date appStartTime = new Date(runtimeBeen.getStartTime());

    @Override
    public Health health() {
        Health.Builder status = Health.up();
        Map<String, Object> details = new HashMap<>();
        details.put("appName", appName);
        details.put("version", appVersion);
        details.put("packagingTime", appPackagingTime);
        details.put("appStartTime", appStartTime);
        details.put("serverTime", LocalDateTime.now());

        return status.withDetails(details).build();
    }
}
