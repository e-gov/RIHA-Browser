package ee.ria.riha.service.util;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Valentin Suhnjov
 */
@Getter
@Setter
public class PagedGridResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private long totalElements;
    private int size;
    private int page;
    private List<T> content = new ArrayList<>();

    public PagedGridResponse() {
    }

    public PagedGridResponse(List<T> content, long totalElements, int size, int page) {
        this.content.addAll(content);
        this.totalElements = totalElements;
        this.size = size;
        this.page = page;
    }

}
