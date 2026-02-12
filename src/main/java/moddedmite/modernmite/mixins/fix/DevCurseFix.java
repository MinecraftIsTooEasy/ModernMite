package moddedmite.modernmite.mixins.fix;

import moddedmite.modernmite.config.ModernMiteConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.Curse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Curse.class)
public class DevCurseFix {
    @ModifyExpressionValue(method = "getRandomCurse", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;inDevMode()Z"))
    private static boolean fix(boolean original) {
        if (ModernMiteConfig.DevCurseFix.getBooleanValue()) return false;
        return original;
    }
}
