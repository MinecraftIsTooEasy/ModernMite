package com.github.Debris.ModernMite.mixins.misc;

import com.github.Debris.ModernMite.config.ModernMiteConfig;
import net.minecraft.LogAgent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LogAgent.class)
public class LogAgentMixin {
    @Inject(method = "setupLogger", at = @At("HEAD"), cancellable = true)
    private void doNotSetup(CallbackInfo ci) {
        if (ModernMiteConfig.NoSpamLog.getBooleanValue()) {
            ci.cancel();
        }
    }
}
