package com.github.skystardust.InputMethodBlocker;

import fi.dy.masa.malilib.ManyLib;
import net.fabricmc.api.ClientModInitializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class InputMethodBlocker implements ClientModInitializer {

    public static boolean available = false;

    @Override
    public void onInitializeClient() {
        try {
            saveNativeFile();
        } catch (IOException e) {
            ManyLib.logger.warn("Modern mite: imblocker fail");
            e.printStackTrace();
        }
        NativeUtils.inactive("");
    }

    public void saveNativeFile() throws IOException {
        OSChecker.OSType osType = OSChecker.getOsType();
        if (osType == OSChecker.OSType.WIN_X64) {
            available = true;
            saveTempNativeFile("InputMethodBlocker-Natives-x64.dll");
        } else if (osType == OSChecker.OSType.WIN_X32) {
            available = true;
            saveTempNativeFile("InputMethodBlocker-Natives-x86.dll");
        }
    }

    private void saveTempNativeFile(String fileName) throws IOException {
        URL resource = getClass().getClassLoader().getResource(fileName);
        if (resource == null) {
            return;
        }
        InputStream fileInputStream = resource.openStream();
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
