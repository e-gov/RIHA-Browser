package ee.ria.riha.service.util;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

/**
 * Response model for paged list.
 *
 * @author Valentin Suhnjov
 */
@Getter
@Setter
public class PagedResponse<T> {

  @Getter(value = AccessLevel.PRIVATE)
  @Setter(value = AccessLevel.PRIVATE)
  private Pageable pageable;

  private long totalElements;
  private List<T> content = new ArrayList<>(0);

  public PagedResponse(Pageable pageable, long totalElements, List<T> content) {
    this(pageable);
    this.totalElements = totalElements;
    this.content = content;
  }

  public PagedResponse(Pageable pageable) {
    Assert.notNull(pageable, "pageable must not be null");

    this.pageable = pageable;
  }

  public PagedResponse() {}

  public int getSize() {
    return pageable.getPageSize();
  }

  public int getPage() {
    return pageable.getPageNumber();
  }

  public int getTotalPages() {
    return getSize() == 0 ? 1 : (int) Math.ceil((double) totalElements / (double) getSize());
  }
}
