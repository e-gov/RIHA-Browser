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
import static org.springframework.util.StringUtils.hasText;

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

    /**
     * Convenience overload of {@link #find(String, Integer, Integer, String, String, String)}
     *
     * @param path data resource path
     * @return list of JSON formatted resource descriptions
     */
    public List<String> find(String path) {
        return find(path, null, null, null, null, null);
    }

    /**
     * Convenience overload of {@link #find(String, Integer, Integer, String, String, String)}
     *
     * @param path   data resource path
     * @param limit  max number of results in response
     * @param offset offset (0 based)
     * @return list of JSON formatted resource descriptions
     */
    public List<String> find(String path, Integer limit, Integer offset) {
        return find(path, limit, offset, null, null, null);
    }

    /**
     * Convenient overload of {@link #find(String, Integer, Integer, String, String, String)}
     *
     * @param path   data resource path
     * @param filter filter definition
     * @param sort   sorting definition
     * @param fields fields definition
     * @return list of JSON formatted resource descriptions
     */
    public List<String> find(String path, String filter, String sort, String fields) {
        return find(path, null, null, filter, sort, fields);
    }

    /**
     * <p>Retrieves data from storage. Capable of paging, filtering and sorting. Returned fields may be restricted to
     * defined set.</p> <p>Filter format definition:
     * <pre>
     * filter               = filter-definition *[ "," filter-definition ]
     * filter-definition    = property-name "," operation "," property-value
     * property-name        = string            ; property to search for
     * operation            = "="
     *                      | ">"
     *                      | "<"
     *                      | ">="
     *                      | "<="
     *                      | "!="
     *                      | "<>"
     *                      | "like"
     *                      | "ilike"
     *                      | "?&"
     *                      | "null_or_>"
     *                      | "null_or_<="
     *                      | "isnull"
     *                      | "isnotnull"
     * property-value       = string            ; filter value
     * Example: name,like,malis,owner,=,70001484
     * </pre>
     * </p> <p>Sort format definition:
     * <pre>
     * sort                 = sort-prefix property-name
     * sort-prefix          = ""        ; sort ascending
     *                      | "-"       ; sort descending
     * property-name        = string    ; property to sort by
     *
     * Example: -name   ; order by name DESC
     * </pre>
     * </p> <p>Fields format definition:
     * <pre>
     * fields               = property-name *[ "," property-name ]
     * property-name        = string    ; property to return
     * </pre>
     * </p>
     *
     * @param path   data resource path
     * @param limit  max number of results in response
     * @param offset offset (0 based)
     * @param filter filter definition
     * @param sort   sorting definition
     * @param fields fields definition
     * @return list of JSON formatted resource descriptions
     */
    public List<String> find(String path, Integer limit, Integer offset, String filter, String sort, String fields) {
        Assert.hasText(path, "path must be specified");

        UriComponentsBuilder uriBuilder = createRequestForPathAndOperation(path, GET);

        if (limit != null) {
            uriBuilder.queryParam("limit", limit);
        }
        if (offset != null) {
            uriBuilder.queryParam("offset", offset);
        }
        if (hasText(filter)) {
            uriBuilder.queryParam("filter", filter);
        }
        if (hasText(sort)) {
            uriBuilder.queryParam("sort", sort);
        }
        if (hasText(fields)) {
            uriBuilder.queryParam("fields", fields);
        }

        List<String> descriptions = new ArrayList<>();

        String responseString = restTemplate.getForObject(uriBuilder.build(false).toUriString(), String.class);
        JSONArray rawArray = new JSONArray(responseString);
        rawArray.forEach(json -> descriptions.add(json.toString()));

        return descriptions;
    }

    private UriComponentsBuilder createRequestForPathAndOperation(String path, OperationType operation) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("path", path)
                .queryParam("op", operation.getUriValue());
    }

    /**
     * Performs count operation on storage with defined filter.
     *
     * @param path   data resource path
     * @param filter filter definition
     * @return number of records
     * @see #find(String, Integer, Integer, String, String, String)
     */
    public long count(String path, String filter) {
        Assert.hasText(path, "path must be specified");

        UriComponentsBuilder uriBuilder = createRequestForPathAndOperation(path, COUNT);
        if (hasText(filter)) {
            uriBuilder.queryParam("filter", filter);
        }

        String responseString = restTemplate.getForObject(uriBuilder.build(false).toUriString(), String.class);
        JSONObject jsonObject = new JSONObject(responseString);

        return jsonObject.getLong("ok");
    }

}
