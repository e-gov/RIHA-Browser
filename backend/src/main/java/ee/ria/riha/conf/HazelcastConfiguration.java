package ee.ria.riha.conf;

import com.hazelcast.config.Config;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;

import java.util.List;

@Configuration
@EnableHazelcastHttpSession
@Slf4j
public class HazelcastConfiguration {
    @Value("${clustering.hazelcast.members:127.0.0.1}")
    private List<String> members;

    @Qualifier("riha")
    @Bean
    public HazelcastInstance hazelcastInstance() {
        log.debug("Configuring Hazelcast");
        HazelcastInstance hazelCastInstance = Hazelcast.getHazelcastInstanceByName("riha");
        if (hazelCastInstance != null) {
            log.debug("Hazelcast already initialized");
            return hazelCastInstance;
        }
        Config config = new Config();
        config.setInstanceName("riha");
        config.getNetworkConfig().setPort(5701);
        config.getNetworkConfig().setPortAutoIncrement(true);

        config.getNetworkConfig().getJoin().getAwsConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        final TcpIpConfig tcpIpconfig = config.getNetworkConfig().getJoin().getTcpIpConfig();
        for (var member : members) {
            tcpIpconfig.addMember(member);
            log.info("[Hazelcast] adding Member: " + member);
        }
        tcpIpconfig.setRequiredMember(null).setEnabled(true);

        return Hazelcast.newHazelcastInstance(config);
    }
}
