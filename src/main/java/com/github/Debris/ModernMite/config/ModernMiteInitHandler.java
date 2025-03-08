package com.github.Debris.ModernMite.config;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;

public class ModernMiteInitHandler implements IInitializationHandler {

    @Override
    public void registerModHandlers() {
        ConfigManager.getInstance().registerConfig(ModernMiteConfig.getInstance());
        Callbacks.init();
    }

}
