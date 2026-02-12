package com.github.skystardust.InputMethodBlocker.compat;

import moddedmite.modernmite.config.ModernMiteConfig;
import net.minecraft.GuiScreen;

public class InputMethodHandler {
    private static final InputMethodHandler Instance = new InputMethodHandler();

    public static InputMethodHandler getInstance() {
        return Instance;
    }

    public boolean shouldActive(GuiScreen gui) {
        if (gui == null) return false;
        return ModernMiteConfig.ForceEnableInputMethodGuiScreens.getStringListValue().contains(gui.getClass().getName());
    }

}
