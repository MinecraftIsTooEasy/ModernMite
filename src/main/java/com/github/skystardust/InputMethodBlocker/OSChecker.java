package com.github.skystardust.InputMethodBlocker;

public class OSChecker {
    public static OSType getOsType() {
        String osName = System.getProperty("os.name").toLowerCase();
        String osArch = System.getProperty("os.arch").toLowerCase();

        if (osName.contains("windows")) {
            if (osArch.contains("64")) {
                return OSType.WIN_X64;
            } else if (osArch.contains("86")) {
                return OSType.WIN_X32;
            }
        }
        return null;
    }

    public enum OSType {
        WIN_X32, WIN_X64
    }
}
