package com.github.skystardust.InputMethodBlocker;


import net.fabricmc.api.ModInitializer;


public class InputMethodBlocker implements ModInitializer {
    @Override
    public void onInitialize() {
        NativeUtils.inactive("");
    }
}
