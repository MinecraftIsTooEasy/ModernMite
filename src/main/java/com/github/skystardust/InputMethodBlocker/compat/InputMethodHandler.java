package com.github.skystardust.InputMethodBlocker.compat;

import com.github.Debris.ModernMite.config.ModernMiteConfig;
import net.minecraft.GuiContainer;
import net.minecraft.GuiScreen;
import net.xiaoyu233.fml.FishModLoader;

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
