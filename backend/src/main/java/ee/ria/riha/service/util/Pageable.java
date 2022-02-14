package ee.ria.riha.service.util;

/**
 * @author Valentin Suhnjov
 */
public interface Pageable {

    int getPageNumber();

    int getPageSize();

    int getOffset();

}
