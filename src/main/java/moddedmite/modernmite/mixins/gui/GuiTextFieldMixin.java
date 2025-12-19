package moddedmite.modernmite.mixins.gui;

import moddedmite.modernmite.config.ModernMiteConfig;
import com.github.skystardust.InputMethodBlocker.NativeUtils;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiTextField.class)
public class GuiTextFieldMixin extends Gui {
    @Shadow private int maxStringLength;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void extendMaxLength(FontRenderer i, int j, int k, int l, int par5, CallbackInfo ci) {
        this.maxStringLength = 256;
    }

    @Inject(method = "setFocused", at = @At("RETURN"))
    private void inputMethodBlocker(boolean focused, CallbackInfo ci) {
        Minecraft client = Minecraft.getMinecraft();
        GuiScreen screen = client.currentScreen;
        NativeUtils.activeOrInactive(focused);
        if (ModernMiteConfig.VanillaChat.getBooleanValue()) {
            if (screen instanceof GuiChat chatScreen) {
                boolean isCommand = chatScreen.defaultInputFieldText.startsWith("/");
                boolean disableForCommands = !ModernMiteConfig.SlashIM.getBooleanValue();
                NativeUtils.activeOrInactive(!(isCommand && disableForCommands));
            }
        } else {
            NativeUtils.active("");
        }
    }
}
