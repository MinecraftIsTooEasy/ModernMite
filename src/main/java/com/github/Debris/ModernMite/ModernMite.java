package com.github.Debris.ModernMite;

import com.github.Debris.ModernMite.config.ModernMiteConfigEarly;
import com.github.Debris.ModernMite.config.ModernMiteInitHandler;
import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ModInitializer;
import net.xiaoyu233.fml.config.ConfigRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ModernMite implements ModInitializer {
    public static final String MOD_ID = "ModernMite";
    public static final Logger LOGGER = LogManager.getLogger(ModernMite.class);

    @Override
    public void onInitialize() {
        InitializationHandler.getInstance().registerInitializationHandler(new ModernMiteInitHandler());
    }

    @Override
    public Optional<ConfigRegistry> createConfig() {
        return Optional.of(ModernMiteConfigEarly.ConfigInstance);
    }
}