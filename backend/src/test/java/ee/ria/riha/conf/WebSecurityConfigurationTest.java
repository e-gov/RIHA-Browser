package ee.ria.riha.conf;

import org.junit.Test;
import org.springframework.web.util.UriUtils;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class WebSecurityConfigurationTest {


    @Test
    public void testUrlProperEncoding() {

        String encoded = UriUtils.encodePath("infosüsteemid", "UTF-8");
        assertThat(encoded, is("infos%C3%BCsteemid"));
    }
}