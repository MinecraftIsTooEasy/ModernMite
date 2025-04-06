package com.github.Debris.ModernMite.mixins.fix;

import com.github.Debris.ModernMite.config.ModernMiteConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.SlotCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SlotCrafting.class)
public class CraftingSpeedFix {
    @ModifyExpressionValue(method = "updatePlayerCrafting", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityClientPlayerMP;getCraftingPeriod(F)I"))
    private int avoidZero(int original) {
        if (original < 25 && ModernMiteConfig.CraftingSpeedFix.getBooleanValue()) {
            original = 25;
        }
        return original;
    }
}
