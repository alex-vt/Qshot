package com.oleksiykovtun.qshot;

import java.awt.*;
import java.io.File;

/**
 * Screen shots folder operations provider
 */
class ScreenShotsFolderManager {
    private static String screenShotsFolderPath = "Screenshots";

    public static void openFolder() {
        try {
            Desktop.getDesktop().open(getScreenShotsFolder());
        } catch (Exception e) {
        }
    }

    public static String getScreenShotsFolderPrefix() {
        return getScreenShotsFolder().getPath() + File.separator;
    }

    private static File getScreenShotsFolder() {
        if (! new File(screenShotsFolderPath).exists()) {
            new File(screenShotsFolderPath).mkdirs();
        }
        return new File(new File(screenShotsFolderPath).getAbsolutePath());
    }
}
