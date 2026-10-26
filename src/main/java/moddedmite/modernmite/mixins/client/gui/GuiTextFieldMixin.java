package moddedmite.modernmite.mixins.client.gui;

import moddedmite.modernmite.config.ModernMiteConfig;
import com.github.skystardust.InputMethodBlocker.NativeUtils;
import net.minecraft.GuiChat;
import net.minecraft.GuiScreen;
import net.minecraft.GuiTextField;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiTextField.class)
public abstract class GuiTextFieldMixin {

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
