package ee.ria.riha.util;

import java.io.File;

public class Utils {
    public static final String RESOURCES_PATH = "/src/test/resources/";

    public static String getFileResourcePath(File file) {
        return file.getAbsoluteFile().getParent() + RESOURCES_PATH + file.getName();
    }
}
