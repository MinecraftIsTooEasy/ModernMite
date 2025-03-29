package com.github.Debris.ModernMite.mixins.fix;

import com.github.Debris.ModernMite.config.ModernMiteConfig;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import fi.dy.masa.malilib.ManyLib;
import net.minecraft.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ResourceLocation.class)
public class ResourceLocationFix {
    @WrapWithCondition(method = "verifyResourceLocations", at = @At(value = "INVOKE", target = "Lnet/minecraft/ResourceLocation;verifyExistence()V"))
    private static boolean fix(ResourceLocation instance) {
        if (instance != null) return true;
        if (ModernMiteConfig.ResourceLocationFix.getBooleanValue()) {
            return false;
        } else {
            ManyLib.logger.warn("ModernMite: caught null resource, but my fix config is off");
            return true;
        }
    }
}
