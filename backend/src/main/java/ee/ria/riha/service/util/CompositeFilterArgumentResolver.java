package ee.ria.riha.service.util;

import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.List;

public class CompositeFilterArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String FILTER_PARAMETER = "filter";
    private static final String SORT_PARAMETER = "sort";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> type = parameter.getParameterType();
        return CompositeFilterRequest.class.isAssignableFrom(type);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        String[] filterParametersFromRequest = webRequest.getParameterValues(FILTER_PARAMETER);
        String[] sortParametersFromRequest = webRequest.getParameterValues(SORT_PARAMETER);

        List<String> filterParameters = filterParametersFromRequest != null ?
                Arrays.asList(filterParametersFromRequest) : null;
        List<String> sortParameters = sortParametersFromRequest != null ?
                Arrays.asList(sortParametersFromRequest) : null;

        return new CompositeFilterRequest(filterParameters, sortParameters);
    }
}
