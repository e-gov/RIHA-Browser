package ee.ria.riha.health;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HeartBeatInfo {
    private String appName;
    private String version;
    private long packagingTime;
    private long appStartTime;
    private long serverTime;
}
