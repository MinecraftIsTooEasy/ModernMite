package com.github.skystardust.InputMethodBlocker;

import net.fabricmc.api.ClientModInitializer;

public class InputMethodBlocker implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        NativeUtils.inactive("");
    }
}
