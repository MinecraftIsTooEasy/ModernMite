package moddedmite.modernmite.mixins.misc;

import moddedmite.modernmite.config.ModernMiteConfig;
import com.github.skystardust.InputMethodBlocker.NativeUtils;
import com.github.skystardust.InputMethodBlocker.compat.InputMethodHandler;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    public EntityClientPlayerMP thePlayer;
    @Shadow
    public GameSettings gameSettings;

    @Shadow
    public abstract void displayGuiScreen(GuiScreen par1GuiScreen);

    @Shadow
    public GuiScreen currentScreen;

    @WrapWithCondition(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/ReferenceFileWriter;write()V"))
    private boolean doNotWriteReferenceFile() {
        return !ModernMiteConfig.NoReferenceFile.getBooleanValue();
    }

    @WrapWithCondition(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/PlayerControllerMP;toggleRun(Lnet/minecraft/EntityPlayer;)V"))
    private boolean sprintingMode(PlayerControllerMP instance, EntityPlayer player) {
        return switch (ModernMiteConfig.SprintingMode.getEnumValue()) {
            case Toggle -> true;
            case Press -> {
                if (this.thePlayer.movementInput.moveForward > 0.0F) {
                    this.thePlayer.setSprinting(true);
                }
                yield false;
            }
        };
    }

    @Inject(method = "displayGuiScreen", at = @At("HEAD"))
    private void chatFix(GuiScreen screen, CallbackInfo ci) {
        NativeUtils.activeOrInactive(InputMethodHandler.getInstance().shouldActive(screen));
    }

    @Inject(method = "openChat", at = @At("TAIL"))
    private void active(GuiChat chatScreen, CallbackInfo ci) {
        if (ModernMiteConfig.VanillaChat.getBooleanValue()) return;
        NativeUtils.activeOrInactive(!(chatScreen.defaultInputFieldText.startsWith("/") && !ModernMiteConfig.SlashIM.getBooleanValue()));
    }

    @Inject(method = "closeImposedChat", at = @At("HEAD"))
    private void inactive(CallbackInfo ci) {
        if (ModernMiteConfig.VanillaChat.getBooleanValue()) return;
        NativeUtils.inactive("");
    }

    @WrapOperation(method = "clickMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiScreen;isCtrlKeyDown()Z", ordinal = 0))
    private boolean noAttackDump(Operation<Boolean> original) {
        return !ModernMiteConfig.NoAttackDump.getBooleanValue() && original.call();
    }

    @Inject(method = "runTick", at = @At("TAIL"))
    public void allowsImposedChat(CallbackInfo ci) {
        if (!ModernMiteConfig.VanillaChat.getBooleanValue()) return;
        while (this.gameSettings.keyBindChat.isPressed() && this.gameSettings.chatVisibility != 2) {
            this.displayGuiScreen(new GuiChat());
        }

        if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed() && this.gameSettings.chatVisibility != 2) {
            this.displayGuiScreen(new GuiChat("/"));
        }
    }
}
