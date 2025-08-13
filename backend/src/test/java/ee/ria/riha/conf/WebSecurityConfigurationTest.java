package ee.ria.riha.conf;

import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class WebSecurityConfigurationTest {


    @Test
    public void testUrlProperEncoding() {

        String encoded = UriUtils.encodePath("infosüsteemid", "UTF-8");
        assertThat(encoded, is("infos%C3%BCsteemid"));
    }
}
