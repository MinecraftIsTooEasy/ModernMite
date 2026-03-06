package moddedmite.modernmite.mixins.fix;

import moddedmite.modernmite.config.ModernMiteConfig;
import net.minecraft.HttpUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HttpUtil.class)
public class HttpUtilMixin {

    @Inject(method = "performGetRequest", at = @At("HEAD"), cancellable = true)
    private static void onPerformGetRequest(String urlString, int connectionTimeout, int readTimeout, CallbackInfoReturnable<String> cir)
    {
        if (ModernMiteConfig.BlockMITEWebRequests.getBooleanValue() && urlString != null && urlString.contains("minecraft-is-too-easy.com"))
        {
            cir.setReturnValue(null);
        }
    }
}