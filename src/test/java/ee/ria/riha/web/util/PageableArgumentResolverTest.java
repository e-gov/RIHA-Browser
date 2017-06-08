package ee.ria.riha.web.util;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Valentin Suhnjov
 */
public class PageableArgumentResolverTest {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int DEFAULT_PAGE_NUMBER = 0;

    private PageableArgumentResolver resolver = new PageableArgumentResolver();
    private MockHttpServletRequest request = new MockHttpServletRequest();

    @Test
    public void usesDefaultSizeIfSizeArgumentIsMissing() {
        PageRequest pageRequest = resolve();
        assertThat(pageRequest.getPageSize(), is(equalTo(DEFAULT_PAGE_SIZE)));
    }

    @Test
    public void usesDefaultSizeIfSizeIsLessThanOne() {
        request.addParameter("size", "0");
        PageRequest pageRequest = resolve();
        assertThat(pageRequest.getPageSize(), is(equalTo(DEFAULT_PAGE_SIZE)));
    }

    @Test
    public void usesGivenSize() {
        request.addParameter("size", "1");
        PageRequest pageRequest = resolve();
        assertThat(pageRequest.getPageSize(), is(equalTo(1)));
    }

    @Test
    public void usesDefaultPageIfPageIsMissing() {
        PageRequest pageRequest = resolve();
        assertThat(pageRequest.getPageNumber(), is(equalTo(DEFAULT_PAGE_NUMBER)));
    }

    @Test
    public void usesDefaultPageIfPageIsLessThanZero() {
        request.addParameter("page", "-1");
        PageRequest pageRequest = resolve();
        assertThat(pageRequest.getPageNumber(), is(equalTo(DEFAULT_PAGE_NUMBER)));
    }

    @Test
    public void usesAllDeafultsWhenArgumentsMissing() {
        PageRequest pageRequest = resolve();

        assertThat(pageRequest.getPageNumber(), is(equalTo(DEFAULT_PAGE_NUMBER)));
        assertThat(pageRequest.getPageSize(), is(equalTo(DEFAULT_PAGE_SIZE)));
    }

    @Test
    public void resolvesAllPageParametersAndCalculatesOffset() {
        request.addParameter("page", "5");
        request.addParameter("size", "3");

        PageRequest pageRequest = resolve();

        assertThat(pageRequest, is(notNullValue()));
        assertThat(pageRequest.getPageSize(), is(equalTo(3)));
        assertThat(pageRequest.getPageNumber(), is(equalTo(5)));
        assertThat(pageRequest.getOffset(), is(equalTo(15)));
    }

    private PageRequest resolve() {
        try {
            return (PageRequest) resolver.resolveArgument(null, null,
                                                          new ServletWebRequest(request), null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}