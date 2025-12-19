package moddedmite.modernmite.mixins.misc;

import moddedmite.modernmite.config.EnumSprintingMode;
import moddedmite.modernmite.config.ModernMiteConfig;
import net.minecraft.GameSettings;
import net.minecraft.Minecraft;
import net.minecraft.MovementInput;
import net.minecraft.MovementInputFromOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MovementInputFromOptions.class)
public class MovementFromOptionsMixin extends MovementInput {
    @Shadow
    private GameSettings gameSettings;

    @Inject(method = "updatePlayerMoveState", at = @At("RETURN"))
    private void betterSprinting(CallbackInfo ci) {
        if (!Minecraft.theMinecraft.thePlayer.isGhost()) {
            if (this.gameSettings.keyBindForward.pressed && ModernMiteConfig.SprintingMode.getEnumValue() == EnumSprintingMode.Press && this.gameSettings.keyBindToggleRun.pressed) {
                if (this.moveForward > 0.0F) {
                    Minecraft.theMinecraft.thePlayer.setSprinting(true);
                }
            }

        }
    }
}
