package ee.ria.riha.web.util;

import lombok.Getter;
import org.springframework.util.Assert;

/**
 * @author Valentin Suhnjov
 */
@Getter
public class PageRequest implements Pageable {

    private final int pageNumber;
    private final int pageSize;
    private int offset;

    public PageRequest(int page, int size) {
        Assert.isTrue(page >= 0, "page must be greater than or equal to zero");
        Assert.isTrue(size > 0, "size must be greater than zero");

        pageNumber = page;
        pageSize = size;
        offset = page * size;
    }
}
