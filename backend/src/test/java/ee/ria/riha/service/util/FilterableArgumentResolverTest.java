package ee.ria.riha.service.util;

import com.sun.jdi.Method;
import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Valentin Suhnjov
 */
public class FilterableArgumentResolverTest {

    private final FilterableArgumentResolver resolver = new FilterableArgumentResolver();
    private final MockHttpServletRequest request = new MockHttpServletRequest();

    @Test
    public void resolvesNonExistentParametersToNull() {
        FilterRequest filterRequest = resolve();

        assertThat(filterRequest.getFilter(), is(nullValue()));
        assertThat(filterRequest.getSort(), is(nullValue()));
        assertThat(filterRequest.getFields(), is(nullValue()));
    }

    @Test
    public void resolvesEmptyParametersAsNull() {
        request.setParameter("filter", "  ");
        request.setParameter("sort", "  ");
        request.setParameter("fields", "  ");

        FilterRequest filterRequest = resolve();

        assertThat(filterRequest.getFilter(), is(nullValue()));
        assertThat(filterRequest.getSort(), is(nullValue()));
        assertThat(filterRequest.getFields(), is(nullValue()));
    }

    @Test
    public void resolvesNonEmptyParametersTrimmed() {
        request.setParameter("filter", " i,like,filter");
        request.setParameter("sort", "-iAmSort ");
        request.setParameter("fields", "  here,are,some,fields  ");

        FilterRequest filterRequest = resolve();

        assertThat(filterRequest.getFilter(), is(equalTo("i,like,filter")));
        assertThat(filterRequest.getSort(), is(equalTo("-iAmSort")));
        assertThat(filterRequest.getFields(), is(equalTo("here,are,some,fields")));
    }

    private FilterRequest resolve() {
        try {
            return (FilterRequest) resolver.resolveArgument(null, null,
                                                          new ServletWebRequest(request), null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
