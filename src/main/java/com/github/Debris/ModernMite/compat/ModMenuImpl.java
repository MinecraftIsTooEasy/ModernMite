package com.github.Debris.ModernMite.compat;

import com.github.Debris.ModernMite.config.ModernMiteConfig;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;

public class ModMenuImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> ModernMiteConfig.getInstance().getConfigScreen(screen);
    }
}
