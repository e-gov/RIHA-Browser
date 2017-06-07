package ee.ria.riha.service;

import ee.ria.riha.service.storage.StorageClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Valentin Suhnjov
 */
@RunWith(MockitoJUnitRunner.class)
public class StorageClientTest {

    private static final String URL = "http://storage/api";

    private RestTemplate restTemplate = mock(RestTemplate.class);

    private StorageClient storageClient = new StorageClient(restTemplate, URL);

    @Before
    public void setUp() throws Exception {
        when(restTemplate.getForObject(anyString(), anyObject()))
                .thenReturn("[{\n" +
                                    "  \"uri\": \"urn:fdc:riha.eesti.ee:2016:infosystem:350811-test\",\n" +
                                    "  \"name\": \"LOADTEST\",\n" +
                                    "  \"owner\": \"70001484\"\n" +
                                    "},\n" +
                                    "{\n" +
                                    "  \"uri\": \"urn:fdc:riha.eesti.ee:2016:classifier:436900\",\n" +
                                    "  \"name\": \"Testklassifikaator\",\n" +
                                    "  \"owner\": \"21304\"\n" +
                                    "}\n" +
                                    "]");
    }

    @Test
    public void producesInfoSystemsFromJson() throws Exception {
        List<String> infoSystems = storageClient.find("path", 5, 0);

        assertThat(infoSystems, hasSize(2));
        assertThat(infoSystems.get(0), containsString("urn:fdc:riha.eesti.ee:2016:infosystem:350811-test"));
        assertThat(infoSystems.get(1), containsString("urn:fdc:riha.eesti.ee:2016:classifier:436900"));
    }

}