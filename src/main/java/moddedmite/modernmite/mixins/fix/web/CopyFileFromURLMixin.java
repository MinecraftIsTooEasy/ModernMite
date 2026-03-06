//package moddedmite.modernmite.mixins.fix.web;
//
//import moddedmite.modernmite.config.ModernMiteConfig;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//@Mixin(targets = "net.minecraft.CopyFileFromURL")
//public class CopyFileFromURLMixin {
//
//    @Inject(method = "copyFileFromURL", at = @At("HEAD"), cancellable = true)
//    private static void onCopyFileFromURL(String url_string, String destination_path, int connection_timeout_ms, int read_timeout_ms, CallbackInfoReturnable<Boolean> cir)
//    {
//        if (ModernMiteConfig.BlockMITEWebRequests.getBooleanValue() && url_string != null && url_string.contains("minecraft-is-too-easy.com"))
//        {
//            cir.setReturnValue(false);
//        }
//    }
//}