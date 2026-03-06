//package moddedmite.modernmite.mixins.fix.web;
//
//import moddedmite.modernmite.config.ModernMiteConfig;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.net.Proxy;
//import java.net.URL;
//import java.net.URLConnection;
//
//@Mixin(URL.class)
//public class URLBlockerMixin {
//
//    @Inject(method = "openConnection*", at = @At("HEAD"), cancellable = true)
//    private void onOpenConnection(CallbackInfoReturnable<URLConnection> cir)
//    {
//        try {
//            String url = this.toString();
//            if (ModernMiteConfig.BlockMITEWebRequests.getBooleanValue() && url.contains("minecraft-is-too-easy.com"))
//            {
//                URLConnection conn = new URLConnection(((URL)(Object)this))
//                {
//                    @Override
//                    public void connect() {}
//
//                    @Override
//                    public InputStream getInputStream() {
//                        return new ByteArrayInputStream(new byte[0]);
//                    }
//                };
//                cir.setReturnValue(conn);
//            }
//        } catch (Throwable ignored) {} // If anything goes wrong, fail safe by not interfering
//    }
//
//    @Inject(method = "openConnection*", at = @At("HEAD"), cancellable = true)
//    private void onOpenConnectionProxy(Proxy proxy, CallbackInfoReturnable<URLConnection> cir)
//    {
//        try {
//            String url = this.toString();
//            if (ModernMiteConfig.BlockMITEWebRequests.getBooleanValue() && url.contains("minecraft-is-too-easy.com"))
//            {
//                URLConnection conn = new URLConnection(((URL)(Object)this))
//                {
//                    @Override
//                    public void connect() {}
//
//                    @Override
//                    public InputStream getInputStream()
//                    {
//                        return new ByteArrayInputStream(new byte[0]);
//                    }
//                };
//                cir.setReturnValue(conn);
//            }
//        }
//        catch (Throwable ignored) {}
//    }
//
//    @Inject(method = "openStream", at = @At("HEAD"), cancellable = true)
//    private void onOpenStream(CallbackInfoReturnable<InputStream> cir)
//    {
//        try {
//            String url = this.toString();
//            if (ModernMiteConfig.BlockMITEWebRequests.getBooleanValue() && url.contains("minecraft-is-too-easy.com"))
//            {
//                cir.setReturnValue(new ByteArrayInputStream(new byte[0]));
//            }
//        } catch (Throwable ignored) {}
//    }
//}