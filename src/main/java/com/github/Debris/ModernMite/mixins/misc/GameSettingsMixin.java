package com.github.Debris.ModernMite.mixins.misc;

import com.github.Debris.ModernMite.CustomKeys;
import net.minecraft.GameSettings;
import net.minecraft.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameSettings.class)
public abstract class GameSettingsMixin {
    @Shadow
    public KeyBinding[] keyBindings;

    @Inject(method = "initKeybindings", at = @At("RETURN"))
    private void inject(CallbackInfo ci) {
        KeyBinding[] vanillaKeyBindings = this.keyBindings;
        KeyBinding[] myKeybindings = CustomKeys.getMyKeybindings();
        KeyBinding[] newKeyBindings = new KeyBinding[vanillaKeyBindings.length + myKeybindings.length];
        System.arraycopy(vanillaKeyBindings, 0, newKeyBindings, 0, vanillaKeyBindings.length);
        System.arraycopy(myKeybindings, 0, newKeyBindings, vanillaKeyBindings.length, myKeybindings.length);
        this.keyBindings = newKeyBindings;
    }
}
