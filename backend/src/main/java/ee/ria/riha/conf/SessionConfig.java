package ee.ria.riha.conf;

import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.web.WebFilter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


import java.util.Properties;

@Component
public class SessionConfig {

    @Bean
    public Config config() {
        return new Config();
    }

    @Bean
    public WebFilter webFilter(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {

        Properties properties = new Properties();
        properties.put("instance-name", hazelcastInstance.getName());
        properties.put("sticky-session", "false");

        return new WebFilter(properties);
    }
}
