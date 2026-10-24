package moddedmite.modernmite.mixins.entity;

import moddedmite.modernmite.config.ModernMiteConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.AbstractClientPlayer;
import net.minecraft.ClientPlayer;
import net.minecraft.Minecraft;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPlayer.class)
public abstract class ClientPlayerMixin extends AbstractClientPlayer {
    public ClientPlayerMixin(World par1World, String par2Str) {
        super(par1World, par2Str);
    }

    @ModifyExpressionValue(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/PlayerControllerMP;isRunToggledOn(Lnet/minecraft/EntityPlayer;)Z"))
    private boolean betterSprinting(boolean original) {
        return switch (ModernMiteConfig.SprintingMode.getEnumValue()) {
            case Press -> Minecraft.getMinecraft().gameSettings.keyBindToggleRun.isPressed();
            case Toggle -> original;
        };
    }
}
