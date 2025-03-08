package com.github.Debris.ModernMite.config;

import com.github.skystardust.InputMethodBlocker.NativeUtils;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import net.minecraft.Minecraft;

public class Callbacks {
    public static void init() {
        ModernMiteConfig.IMBlocker.setValueChangeCallback(configBoolean -> {
            if (!configBoolean.getBooleanValue()) NativeUtils.forceActive("");
        });
        ModernMiteConfig.OpenWindow.getKeybind().setCallback(new OpenWindow());
    }

    private static class OpenWindow implements IHotkeyCallback {
        @Override
        public boolean onKeyAction(KeyAction keyAction, IKeybind iKeybind) {
            Minecraft.getMinecraft().displayGuiScreen(ModernMiteConfig.getInstance().getConfigScreen(null));
            return true;
        }
    }
}
