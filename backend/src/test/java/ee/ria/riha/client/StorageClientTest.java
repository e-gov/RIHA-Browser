package ee.ria.riha.client;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ee.ria.riha.service.util.FilterRequest;
import ee.ria.riha.service.util.PageRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

/**
 * @author Valentin Suhnjov
 */
@RunWith(MockitoJUnitRunner.class)
public class StorageClientTest {

    private static final String URL = "http://storage/api";

    @Spy
    private RestTemplate restTemplate = mock(RestTemplate.class);

    @InjectMocks
    private StorageClient storageClient = new StorageClient(URL);

    @Before
    public void setUp() throws JsonProcessingException {
        List<String> response = new ArrayList<>();
        response.add("{\n" +
                             "  \"uri\": \"urn:fdc:riha.eesti.ee:2016:infosystem:350811-test\",\n" +
                             "  \"name\": \"LOADTEST\",\n" +
                             "  \"owner\": \"70001484\"\n" +
                             "}");
        response.add("{\n" +
                             "  \"uri\": \"urn:fdc:riha.eesti.ee:2016:classifier:436900\",\n" +
                             "  \"name\": \"Testklassifikaator\",\n" +
                             "  \"owner\": \"21304\"\n" +
                             "}");

        String json = "{ \"ok\" : \"200\" } ";
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(json);


        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(),
            ArgumentMatchers.<ParameterizedTypeReference<List<String>>>any())).thenReturn(ResponseEntity.ok(response));

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(),
                ArgumentMatchers.<ParameterizedTypeReference<JsonNode>>any())).thenReturn(ResponseEntity.ok(jsonNode));

        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(),
                ArgumentMatchers.<ParameterizedTypeReference<JsonNode>>any())).thenReturn(ResponseEntity.ok(jsonNode));
    }

    @Test
    public void dividesResponseArrayToListOfInfoSystemJSONs() {
        List<String> infoSystems = storageClient.find("path", String.class);

        assertThat(infoSystems, hasSize(2));
        assertThat(infoSystems.get(0), containsString("urn:fdc:riha.eesti.ee:2016:infosystem:350811-test"));
        assertThat(infoSystems.get(1), containsString("urn:fdc:riha.eesti.ee:2016:classifier:436900"));
    }

    @Test
    public void includesPagingAndFilteringParametersDuringSearch() {
        storageClient.find("path", new PageRequest(5, 3),
                           new FilterRequest("name,ilike,TestSystem", "-modification_date", "owner,name"),
                           String.class);

        verify(restTemplate).exchange(
                argThat(allOf(
                        containsString("limit=3"),
                        containsString("offset=15"),
                        containsString("filter=name,ilike,TestSystem"),
                        containsString("sort=-modification_date"),
                        containsString("fields=owner,name")
                )),
                eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.<ParameterizedTypeReference<List<String>>>any());
    }

    @Test
    public void update(){
        Long update = storageClient.update("path", 1L, "asd");

        assertThat(update, is(equalTo(200L)));
    }

    @Test
    public void remove(){
        Long remove = storageClient.remove("path", 1L);

        assertThat(remove, is(equalTo(200L)));
    }
}
