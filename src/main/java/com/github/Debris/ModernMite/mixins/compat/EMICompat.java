package com.github.Debris.ModernMite.mixins.compat;

import com.github.skystardust.InputMethodBlocker.NativeUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shims.java.net.minecraft.client.gui.widget.TextFieldWidget;

@Mixin(TextFieldWidget.class)
public class EMICompat {
    @Inject(method = "setFocused", at = @At("HEAD"))
    private void activateIM(boolean focused, CallbackInfo ci) {
        if (focused) {
            NativeUtils.active("");
        } else {
            NativeUtils.inactive("");
        }
    }
}
