package ee.ria.riha.web.util;

/**
 * @author Valentin Suhnjov
 */
public interface Pageable {

    int getPageNumber();

    int getPageSize();

    int getOffset();

}
