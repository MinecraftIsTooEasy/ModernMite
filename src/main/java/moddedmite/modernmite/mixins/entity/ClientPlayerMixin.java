package moddedmite.modernmite.mixins.entity;

import moddedmite.modernmite.config.ModernMiteConfig;
import moddedmite.modernmite.internal.chat.ChatGuiAccess;
import moddedmite.modernmite.internal.chat.ChatLinkSupport;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.AbstractClientPlayer;
import net.minecraft.ChatMessageComponent;
import net.minecraft.ClientPlayer;
import net.minecraft.Minecraft;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayer.class)
public abstract class ClientPlayerMixin extends AbstractClientPlayer {
    public ClientPlayerMixin(World par1World, String par2Str) {
        super(par1World, par2Str);
    }

    @Inject(method = "sendChatToPlayer", at = @At("HEAD"), cancellable = true)
    private void modernMite$preserveChatLinks(ChatMessageComponent component, CallbackInfo ci) {
        if (!ChatLinkSupport.hasOpenUrl(component)) {
            return;
        }
        ((ChatGuiAccess) Minecraft.getMinecraft().ingameGUI.getChatGUI())
                .modernMite$printChatMessage(component);
        ci.cancel();
    }

    @ModifyExpressionValue(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/PlayerControllerMP;isRunToggledOn(Lnet/minecraft/EntityPlayer;)Z"))
    private boolean betterSprinting(boolean original) {
        return switch (ModernMiteConfig.SprintingMode.getEnumValue()) {
            case Press -> Minecraft.getMinecraft().gameSettings.keyBindToggleRun.isPressed();
            case Toggle -> original;
        };
    }
}
