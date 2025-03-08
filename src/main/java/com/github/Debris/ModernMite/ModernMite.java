package com.github.Debris.ModernMite;

import com.github.Debris.ModernMite.config.ModernMiteConfigEarly;
import com.github.Debris.ModernMite.config.ModernMiteInitHandler;
import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ModInitializer;
import net.xiaoyu233.fml.config.ConfigRegistry;

import java.util.Optional;

public class ModernMite implements ModInitializer {
    public static final String MOD_ID = "ModernMite";

    @Override
    public void onInitialize() {
        InitializationHandler.getInstance().registerInitializationHandler(new ModernMiteInitHandler());
    }

    @Override
    public Optional<ConfigRegistry> createConfig() {
        return Optional.of(ModernMiteConfigEarly.ConfigInstance);
    }
}