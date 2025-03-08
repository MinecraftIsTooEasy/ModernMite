package com.github.skystardust.InputMethodBlocker;

import com.github.Debris.ModernMite.config.ModernMiteConfig;

public class NativeUtils {
    public static boolean available = false;

    private static boolean shouldPerform() {
        return available && ModernMiteConfig.IMBlocker.getBooleanValue();
    }

    public static void forceInactive(String windowName){
        if (available) {
            inactiveInputMethod(windowName);
        }
    }

    public static void forceActive(String windowName) {
        if (available) activeInputMethod(windowName);
    }

    public static void inactive(String windowName) {
        if (shouldPerform()) inactiveInputMethod(windowName);
    }

    public static void active(String windowName) {
        if (shouldPerform()) activeInputMethod(windowName);
    }

    private static native void inactiveInputMethod(String windowName);

    private static native void activeInputMethod(String windowName);
}
