package com.github.Debris.ModernMite.mixins.gui;

import com.github.skystardust.InputMethodBlocker.NativeUtils;
import net.minecraft.GuiTextField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiTextField.class)
public class GuiTextFieldMixin {
    @Inject(method = "setFocused", at = @At("RETURN"))
    private void inputMethod(boolean par1, CallbackInfo ci) {
        if (par1) {
            NativeUtils.active("");
        } else {
            NativeUtils.inactive("");
        }
    }
}
