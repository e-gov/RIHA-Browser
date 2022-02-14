package ee.ria.riha.service.util;

import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Simple page request argument resolver that resolves page number and size.
 *
 * @author Valentin Suhnjov
 */
public class PageableArgumentResolver implements HandlerMethodArgumentResolver {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final String PAGE_PARAMETER = "page";
    private static final String SIZE_PARAMETER = "size";

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> type = parameter.getParameterType();
        return Pageable.class.isAssignableFrom(type);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String pageString = webRequest.getParameter(PAGE_PARAMETER);
        String sizeString = webRequest.getParameter(SIZE_PARAMETER);

        int page = parseOrGetDefault(pageString, DEFAULT_PAGE);
        page = page < 0 ? DEFAULT_PAGE : page;

        int size = parseOrGetDefault(sizeString, DEFAULT_SIZE);
        size = size < 1 ? DEFAULT_SIZE : size;

        return new PageRequest(page, size);
    }

    private int parseOrGetDefault(String stringValue, int defaultValue) {
        return StringUtils.hasText(stringValue) ? Integer.parseInt(stringValue) : defaultValue;
    }

}
