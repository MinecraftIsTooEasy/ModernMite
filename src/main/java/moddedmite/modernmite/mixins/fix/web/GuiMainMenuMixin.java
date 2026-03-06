//package moddedmite.modernmite.mixins.fix.web;
//
//import moddedmite.modernmite.config.ModernMiteConfig;
//import net.minecraft.GuiButton;
//import net.minecraft.GuiMainMenu;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@Mixin(GuiMainMenu.class)
//public class GuiMainMenuMixin {
//
//    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
//    private void blockMITEWebButton(GuiButton par1GuiButton, CallbackInfo ci)
//    {
//        if (ModernMiteConfig.BlockMITEWebRequests.getBooleanValue() && par1GuiButton.id == 6)
//        {
//            ci.cancel();
//        }
//    }
//}