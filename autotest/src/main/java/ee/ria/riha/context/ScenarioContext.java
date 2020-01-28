package ee.ria.riha.context;

import java.util.*;

public class ScenarioContext {
    public static final String APP_URL_KEY = "appUrl";
    public static final String SEARCH_TEXT_KEY = "searchText";
    public static final String NAME_KEY = "name";
    public static final String SHORT_NAME_KEY = "shortName";
    public static final String HOMEPAGE_KEY = "homepage";
    public static final String PURPOSE_KEY = "purpose";

    public static final String LAST_INFOSYSTEM_NUMBER = "lastInfosystemNumber";

    public static final String CREATED_SYSTEM_NAME = "CREATED_SYSTEM_NAME";
    public static final String CREATED_SYSTEM_SHORT_NAME = "CREATED_SYSTEM_SHORT_NAME";

    public static final String DEFAULT_APP_URL = "https://riha-browser-ik.ci.kit";

    private final Map<String, String> context = new HashMap<>();

    public ScenarioContext() {
        String url = System.getProperty("url");
        if (url == null) {
            url = DEFAULT_APP_URL;
        }
        context.put(APP_URL_KEY, url);
    }

    public void saveToContext(String key, String value) {
        context.put(key, value);
    }

    public String getFromContext(String key) {
        return context.get(key);
    }

    public void clear() {
        context.clear();
    }
}
