package ee.ria.riha.web.util;

import lombok.Getter;
import lombok.ToString;
import org.springframework.util.Assert;

/**
 * @author Valentin Suhnjov
 */
@Getter
@ToString
public class PageRequest implements Pageable {

    private int pageNumber;
    private int pageSize;
    private int offset;

    public PageRequest(int page, int size) {
        Assert.isTrue(page >= 0, "page must be greater than or equal to zero");
        Assert.isTrue(size > 0, "size must be greater than zero");

        pageNumber = page;
        pageSize = size;
        offset = page * size;
    }

}
