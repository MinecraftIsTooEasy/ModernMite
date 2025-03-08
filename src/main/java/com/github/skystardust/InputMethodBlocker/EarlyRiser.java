package com.github.skystardust.InputMethodBlocker;

import fi.dy.masa.malilib.ManyLib;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class EarlyRiser implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        try {
            saveNativeFile();
        } catch (IOException e) {
            ManyLib.logger.warn("Modern mite: imblocker fail");
            e.printStackTrace();
        }
    }

    public void saveNativeFile() throws IOException {
        OSChecker.OSType osType = OSChecker.getOsType();
        if (osType == OSChecker.OSType.WIN_X64) {
            NativeUtils.available = true;
            saveTempNativeFile("InputMethodBlocker-Natives-x64.dll");
        } else if (osType == OSChecker.OSType.WIN_X32) {
            NativeUtils.available = true;
            saveTempNativeFile("InputMethodBlocker-Natives-x86.dll");
        }
    }


    private void saveTempNativeFile(String fileName) throws IOException {
        InputStream fileInputStream = getClass().getClassLoader().getResource(fileName).openStream();
        File nativeFile = File.createTempFile("InputMethodBlocker", ".dll");
        FileOutputStream out = new FileOutputStream(nativeFile);
        int i;
        byte[] buf = new byte[1024];
        while ((i = fileInputStream.read(buf)) != -1) {
            out.write(buf, 0, i);
        }
        fileInputStream.close();
        out.close();
        nativeFile.deleteOnExit();
        System.load(nativeFile.toString());
    }
}
