package moddedmite.modernmite.mixins.network;

import com.llamalad7.mixinextras.sugar.Local;
import moddedmite.modernmite.config.ModernMiteConfig;
import moddedmite.modernmite.internal.chat.ChatGuiAccess;
import moddedmite.modernmite.internal.chat.ChatLinkSupport;
import net.minecraft.ChatMessageComponent;
import net.minecraft.EntityLiving;
import net.minecraft.Minecraft;
import net.minecraft.NetClientHandler;
import net.minecraft.Packet3Chat;
import net.minecraft.Packet24MobSpawn;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetClientHandler.class)
public class NetClientHandlerMixin {
    @Shadow
    private Minecraft mc;

    @Inject(method = "handleChat", at = @At("HEAD"), cancellable = true)
    private void modernMite$preserveChatLinks(Packet3Chat packet, CallbackInfo ci) {
        ChatMessageComponent component = ChatMessageComponent.createFromJson(packet.message);
        if (!ChatLinkSupport.hasOpenUrl(component)) {
            return;
        }
        ((ChatGuiAccess) this.mc.ingameGUI.getChatGUI()).modernMite$printChatMessage(component);
        ci.cancel();
    }

    @Inject(
            method = "handleMobSpawn",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/EntityLiving;rotationYawHead:F",
                    opcode = Opcodes.PUTFIELD
            ),
            cancellable = true
    )
    private void fix(Packet24MobSpawn par1Packet24MobSpawn, CallbackInfo ci, @Local EntityLiving var10) {
        if (var10 == null && ModernMiteConfig.Packet24Fix.getBooleanValue()) {
            ci.cancel();
            Minecraft.setErrorMessage(String.format("服务器传来type=%d的实体, 但客户端无法匹配", par1Packet24MobSpawn.type));
        }
    }
}
