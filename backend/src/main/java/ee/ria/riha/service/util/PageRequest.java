package ee.ria.riha.service.util;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

/**
 * @author Valentin Suhnjov
 */
@Getter
@ToString
public class PageRequest implements Pageable {

    private final int pageNumber;
    private final int pageSize;
    private final int offset;

    public PageRequest(int page, int size) {
        Assert.isTrue(page >= 0, "page must be greater than or equal to zero");
        Assert.isTrue(size > 0, "size must be greater than zero");

        pageNumber = page;
        pageSize = size;
        offset = page * size;
    }

}
