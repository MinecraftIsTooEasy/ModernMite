//package moddedmite.modernmite.mixins.fix.web;
//
//import moddedmite.modernmite.config.ModernMiteConfig;
//
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(targets = "net.minecraft.GetPublicServers")
//public class BlockMITEWebRequestsMixin {
//
//    @Inject(method = "run", at = @At("HEAD"), cancellable = true)
//    private void blockPublicServersDownload(CallbackInfo ci)
//    {
//        if (ModernMiteConfig.BlockMITEWebRequests.getBooleanValue())
//        {
//            ci.cancel();
//        }
//    }
//}