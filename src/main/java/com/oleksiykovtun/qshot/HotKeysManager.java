package com.oleksiykovtun.qshot;

import com.tulskiy.keymaster.common.Provider;

import javax.swing.*;

/**
 * Screen shots taking hot keys functionality
 */
class HotKeysManager {
    private static String screenShotTakingHotKey = "control Q";

    public static void registerScreenShotTakingHotKey() {
        Provider globalHotKeysProvider = Provider.getCurrentProvider(false);
        globalHotKeysProvider.register(KeyStroke.getKeyStroke(screenShotTakingHotKey),
            HotKeyListener->ScreenShotTakingWindow.show());
    }
}
