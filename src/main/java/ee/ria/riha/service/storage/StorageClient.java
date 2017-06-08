package ee.ria.riha.service.storage;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

import static ee.ria.riha.service.storage.OperationType.COUNT;
import static ee.ria.riha.service.storage.OperationType.GET;

/**
 * Makes requests to RIHA-Storage for data.
 *
 * @author Valentin Suhnjov
 */
public class StorageClient {

    public static final String MAIN_RESOURCE_PATH = "db/main_resource";

    private RestTemplate restTemplate;
    private String baseUrl;

    public StorageClient(RestTemplate restTemplate, String baseUrl) {
        Assert.notNull(restTemplate, "restTemplate must be provided");
        Assert.notNull(baseUrl, "baseUrl must be provided");
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public List<String> find(String path, int limit, int offset) {
        UriComponentsBuilder uriBuilder = createRequestForPathAndOperation(path, GET)
                .queryParam("limit", limit)
                .queryParam("offset", offset);

        List<String> descriptions = new ArrayList<>();

        String responseString = restTemplate.getForObject(uriBuilder.toUriString(), String.class);
        JSONArray rawArray = new JSONArray(responseString);
        rawArray.forEach(json -> descriptions.add(json.toString()));

        return descriptions;
    }

    private UriComponentsBuilder createRequestForPathAndOperation(String path, OperationType operation) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("path", path)
                .queryParam("op", operation.getUriValue());
    }

    public long count(String path) {
        UriComponentsBuilder uriBuilder = createRequestForPathAndOperation(path, COUNT);
        String responseString = restTemplate.getForObject(uriBuilder.toUriString(), String.class);

        JSONObject jsonObject = new JSONObject(responseString);
        return jsonObject.getLong("ok");
    }

}
