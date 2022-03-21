package ee.ria.riha.service.util;

import ee.ria.riha.client.OperationType;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author Valentin Suhnjov
 */
public class StorageRepositoryUriHelper {

    public static UriComponentsBuilder createRequestForPathAndOperation(String baseUrl, String path,
                                                                        OperationType operation) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("path", path)
                .queryParam("op", operation.getValue());
    }

    public static void setFilter(UriComponentsBuilder uriBuilder, Pageable pageable, Filterable filterable) {
        if (pageable != null) {
            uriBuilder.queryParam("limit", pageable.getPageSize());
            uriBuilder.queryParam("offset", pageable.getOffset());
        }

        if (filterable != null) {
            if (filterable.getFilter() != null) {
                uriBuilder.queryParam("filter", filterable.getFilter());
            }
            if (filterable.getSort() != null) {
                uriBuilder.queryParam("sort", filterable.getSort());
            }
            if (filterable.getFields() != null) {
                uriBuilder.queryParam("fields", filterable.getFields());
            }
        }
    }

    public static void setCompositeFilter(UriComponentsBuilder uriBuilder, Pageable pageable,
                                          CompositeFilterRequest filterRequest) {
        if (pageable != null) {
            uriBuilder.queryParam("size", pageable.getPageSize());
            uriBuilder.queryParam("page", pageable.getPageNumber());
        }

        if (filterRequest != null) {
            if (filterRequest.getFilterParameters() != null) {
                for (String filter : filterRequest.getFilterParameters()) {
                    if (filter != null) {
                        uriBuilder.queryParam("filter", filter);
                    }
                }
            }

            if (filterRequest.getSortParameters() != null) {
                for (String sort : filterRequest.getSortParameters()) {
                    if (sort != null) {
                        uriBuilder.queryParam("sort", sort);
                    }
                }
            }
        }
    }

}
