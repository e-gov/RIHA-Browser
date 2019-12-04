package ee.ria.riha.pages;

import ee.ria.riha.context.ScenarioContext;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PageFactory {
    private ScenarioContext scenarioContext;
    private Map<Class, Object> pages;

    public PageFactory() {
        pages = new HashMap<>();
        scenarioContext = new ScenarioContext();
    }

    public <T extends BasePage> T getPage(Class<T> pageClass) {
        try {
            T page = (T) pages.get(pageClass);
            if (page == null) {
                page = pageClass.getDeclaredConstructor(ScenarioContext.class).newInstance(scenarioContext);
                pages.put(pageClass, page);
            }
            return page;
        } catch (Exception e) {
            log.error("Error creating page", e);
            throw new RuntimeException("Error creating page");
        }
    }
}
