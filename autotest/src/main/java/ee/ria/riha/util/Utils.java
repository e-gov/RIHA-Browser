package ee.ria.riha.util;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.io.*;

public class Utils {
    public static final String RESOURCES_PATH = "/src/test/resources/";

    public static String getFileResourcePath(File file) {
        return file.getAbsoluteFile().getParent() + RESOURCES_PATH + file.getName();
    }

    public static void modifiedSelectByValue(Select select, String valueToSelect) {
        WebElement optionToSelect = select.getOptions()
                .stream()
                .filter(webElement -> webElement.getAttribute("value")
                .endsWith(valueToSelect)).findFirst().orElseThrow(IllegalStateException::new);
        select.selectByValue(optionToSelect.getAttribute("value"));
    }
}
