package ee.ria.riha.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ee.ria.riha.domain.model.Comment;
import ee.ria.riha.service.ObjectNotFoundException;
import ee.ria.riha.service.util.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import static ee.ria.riha.client.OperationType.*;
import static ee.ria.riha.service.util.StorageRepositoryUriHelper.createRequestForPathAndOperation;

/**
 * Makes requests to RIHA-Storage for data.
 *
 * @author Valentin Suhnjov
 */
public class StorageClient {

    private static final String MESSAGE_PATH_MUST_BE_SPECIFIED = "path must be specified";

    private RestTemplate restTemplate;
    private final String baseUrl;

    public StorageClient(String baseUrl) {
        Assert.notNull(baseUrl, "baseUrl must be provided");
        this.baseUrl = baseUrl;

        createRestTemplate();
    }

    private void createRestTemplate() {
        this.restTemplate = new RestTemplate();
        this.restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        this.restTemplate.setErrorHandler(new StorageResponseErrorHandler());
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Convenient overload of {@link #find(String, Pageable, Filterable, Class)}
     *
     * @param path         data resource path
     * @param responseType the type of the returned resource
     * @return list of found resources
     */
    public <T> List<T> find(String path, Class<T> responseType) {
        return find(path, null, null, responseType);
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
     *                      | "jilike"
     *                      | "jarr"
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
     * @param path         data resource path
     * @param pageable     pagination
     * @param filterable   filtering, sorting and fields
     * @param responseType the type of the returned resource
     * @return list of found resources
     */
    public <T> List<T> find(String path, Pageable pageable, Filterable filterable, final Class<T> responseType) {
        Assert.hasText(path, MESSAGE_PATH_MUST_BE_SPECIFIED);

        UriComponentsBuilder uriBuilder = createRequestForPathAndOperation(baseUrl, path, GET);
        StorageRepositoryUriHelper.setFilter(uriBuilder, pageable, filterable);

        ParameterizedTypeReference<List<T>> listResponseType = new ParameterizedTypeReference<>() {
            @Override
            @NonNull
            public Type getType() {
                return new ParameterizedListTypeReference((ParameterizedType) super.getType(),
                        new Type[]{responseType});
            }
        };

        ResponseEntity<List<T>> responseEntity = restTemplate.exchange(uriBuilder.build(false).toUriString(),
                HttpMethod.GET, null,
                listResponseType);

        return responseEntity.getBody();
    }

    /**
     * Convenient overload of {@link #find(String, Pageable, Filterable, Class)}
     *
     * @param path         data resource path
     * @param pageable     pagination
     * @param responseType the type of the returned resource
     * @return list of found resources
     */
    public <T> List<T> find(String path, Pageable pageable, Class<T> responseType) {
        return find(path, pageable, null, responseType);
    }

    /**
     * Convenient overload of {@link #find(String, Pageable, Class)}
     *
     * @param path         data resource path
     * @param filterable   filtering, sorting and fields
     * @param responseType the type of the returned resource
     * @return list of found resources
     */
    public <T> List<T> find(String path, Filterable filterable, Class<T> responseType) {
        return find(path, null, filterable, responseType);
    }

    /**
     * Stores json entity in the storage.
     *
     * @param path   data resource path
     * @param entity entity model
     * @return ids of created entities
     */
    public List<Long> create(String path, Object entity) {
        return postRequest(path, entity, POST, new ParameterizedTypeReference<>() {
        });
    }

    private <T> T postRequest(String path, Object entity, OperationType operationType,
                              ParameterizedTypeReference<T> responseType) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl);

        ObjectNode request = JsonNodeFactory.instance.objectNode();
        request.put("op", operationType.getValue());
        request.put("path", path);
        if (entity != null) {
            request.putPOJO("data", entity);
        }

        ResponseEntity<T> responseEntity = restTemplate.exchange(uriBuilder.toUriString(),
                HttpMethod.POST,
                new HttpEntity<Object>(request),
                responseType
        );

        return responseEntity.getBody();
    }

    /**
     * Retrieves single record with given id. Creates request in form
     * <pre>
     * request-parameters = "path=" resource-path "/" record-id "&op=get"
     * </pre>
     *
     * @param path         data resource path
     * @param id           an id of a record
     * @param responseType the type of the return value
     * @return single record with given id
     */
    public <T> T get(String path, Long id, Class<T> responseType) {
        UriComponentsBuilder uriBuilder = createRequestForPathAndOperation(baseUrl, path + "/" + id.toString(), GET);
        return restTemplate.getForObject(uriBuilder.toUriString(), responseType);
    }

    /**
     * Retrieve paged records and converts them to specified type.
     *
     * @param path         data resource path
     * @param pageable     paging information
     * @param filterable   filtering information
     * @param responseType concrete class for result binding
     * @return paged response
     */
    public <T> PagedResponse<T> list(String path, Pageable pageable, Filterable filterable,
                                     Class<T> responseType) {
        Assert.hasText(path, MESSAGE_PATH_MUST_BE_SPECIFIED);

        PagedResponse<T> response = new PagedResponse<>(pageable);

        long totalElements = count(path, filterable);
        response.setTotalElements(totalElements);

        if (totalElements > 0) {
            response.setContent(find(path, pageable, filterable, responseType));
        }

        return response;
    }

    /**
     * Performs count operation on storage with defined filter.
     *
     * @param path       data resource path
     * @param filterable filter definition
     * @return number of records
     * @see #find(String, Pageable, Filterable, Class)
     */
    public long count(String path, Filterable filterable) {
        Assert.hasText(path, MESSAGE_PATH_MUST_BE_SPECIFIED);

        UriComponentsBuilder uriBuilder = createRequestForPathAndOperation(baseUrl, path, COUNT);
        StorageRepositoryUriHelper.setFilter(uriBuilder, null, filterable);

        JsonNode response = restTemplate.getForObject(uriBuilder.build(false).toUriString(), JsonNode.class);

        if (response != null){
            return response.get("ok").asLong();
        } else {
            return 0L;
        }
    }

    public PagedGridResponse<Comment> list(String path, CompositeFilterRequest filterRequest, Pageable pageable) {
        Assert.hasText(path, MESSAGE_PATH_MUST_BE_SPECIFIED);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl).path(path);
        StorageRepositoryUriHelper.setCompositeFilter(uriBuilder, pageable, filterRequest);

        ResponseEntity<PagedGridResponse<Comment>> responseEntity = restTemplate.exchange(
                uriBuilder.build(false).toUriString(), HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });

        return responseEntity.getBody();
    }

    /**
     * Updates existing entity in the storage.
     *
     * @param path   data resource path
     * @param id     an id of a record
     * @param entity entity model  @return ids of updated entitites
     * @return number of updated records
     */
    public Long update(String path, Long id, Object entity) {

        JsonNode response = postRequest(path + "/" + id.toString(), entity, PUT,
                new ParameterizedTypeReference<>() {
                });

        if (Objects.nonNull(response)){
            return response.get("ok").asLong();
        } else throw new ObjectNotFoundException("Response not found for "+ PUT +" request: " + path + "/" + id);
    }

    /**
     * Removes existing entity in the storage using provided id
     *
     * @param path data resource path
     * @param id   ad id of a record
     * @return number of deleted records
     */
    public Long remove(String path, Long id) {
        JsonNode response = postRequest(path + "/" + id.toString(), null, DELETE,
                new ParameterizedTypeReference<>() {
                });

        if (Objects.nonNull(response)){
            return response.get("ok").asLong();
        } else throw new ObjectNotFoundException("Response not found for" + DELETE + "request: " + path + "/" + id);
    }

    public static class ParameterizedListTypeReference implements ParameterizedType {
        private final ParameterizedType delegate;
        private final Type[] actualTypeArguments;

        ParameterizedListTypeReference(ParameterizedType delegate, Type[] actualTypeArguments) {
            this.delegate = delegate;
            this.actualTypeArguments = actualTypeArguments;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return actualTypeArguments;
        }

        @Override
        public Type getRawType() {
            return delegate.getRawType();
        }

        @Override
        public Type getOwnerType() {
            return delegate.getOwnerType();
        }
    }

}
