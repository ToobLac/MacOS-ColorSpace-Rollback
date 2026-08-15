package me.calboot.mcsr;

import ca.weblite.objc.Client;
import ca.weblite.objc.Proxy;

import java.util.Locale;

public class MacosUtil {

    public static final boolean IS_MACOS = System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("mac");

    public static void setMainWindowColorSpace() {
        Proxy window = Client.getInstance()
                .sendProxy("NSApplication", "sharedApplication")
                .sendProxy("windows")
                .sendProxy("firstObject");
        window.send("setColorSpace:", window.getClient().send("NSColorSpace", "displayP3ColorSpace"));
    }

}
