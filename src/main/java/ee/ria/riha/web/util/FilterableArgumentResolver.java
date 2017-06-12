package ee.ria.riha.web.util;

import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author Valentin Suhnjov
 */
public class FilterableArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String SORT_PARAMETER = "sort";
    private static final String FILTER_PARAMETER = "filter";
    private static final String FIELDS_PARAMETER = "fields";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> type = parameter.getParameterType();
        return Filterable.class.isAssignableFrom(type);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String sortString = webRequest.getParameter(SORT_PARAMETER);
        String filterString = webRequest.getParameter(FILTER_PARAMETER);
        String fieldsString = webRequest.getParameter(FIELDS_PARAMETER);

        String filter = StringUtils.hasText(filterString) ? filterString.trim() : null;
        String sort = StringUtils.hasText(sortString) ? sortString.trim() : null;
        String fields = StringUtils.hasText(fieldsString) ? fieldsString.trim() : null;

        return new FilterRequest(filter, sort, fields);
    }
}
