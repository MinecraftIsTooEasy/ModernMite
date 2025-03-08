package com.github.Debris.ModernMite.mixins.misc;

import com.github.Debris.ModernMite.CustomKeys;
import com.github.Debris.ModernMite.config.ModernMiteConfig;
import com.github.skystardust.InputMethodBlocker.NativeUtils;
import com.github.skystardust.InputMethodBlocker.compat.InputMethodHandler;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.*;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    public EntityClientPlayerMP thePlayer;

    @ModifyArg(method = "screenshotListener", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;isKeyDown(I)Z"))
    private int modifyPrintScreenKey(int key) {
        return CustomKeys.printScreenKeyProvider();
    }

    @ModifyArg(method = "screenshotListenerForForcedRendering", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;isKeyDown(I)Z"))
    private int modifyPrintScreenKey_1(int key) {
        return CustomKeys.printScreenKeyProvider();
    }

    @ModifyConstant(method = "runTick", constant = @Constant(intValue = 63))
    private int modifyPersonViewKey(int key) {
        return CustomKeys.personViewKeyProvider();
    }

    @Redirect(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/InventoryPlayer;currentItem:I", opcode = Opcodes.PUTFIELD))
    private void disableVanillaItemSwitch(InventoryPlayer instance, int value) {
    }

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

    @Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/GameSettings;showDebugInfo:Z", opcode = Opcodes.GETFIELD, ordinal = 2))
    private void customItemSwitch(CallbackInfo ci) {
        int var1;
        for (var1 = 0; var1 < 9; ++var1) {
            if (Keyboard.getEventKey() == CustomKeys.inventoryKeyProvider(var1)) {
                this.thePlayer.inventory.currentItem = var1;
                break;
            }
        }
    }

    @Inject(method = "displayGuiScreen", at = @At("HEAD"))
    private void chatFix(GuiScreen gui, CallbackInfo ci) {
        if (InputMethodHandler.getInstance().shouldActive(gui)) {
            NativeUtils.active("");
        } else {
            NativeUtils.inactive("");
        }
    }

    @Inject(method = "openChat", at = @At("TAIL"))
    private void active(GuiChat gui_chat, CallbackInfo ci) {
        if (gui_chat.defaultInputFieldText.startsWith("/") && !ModernMiteConfig.SlashIM.getBooleanValue()) {
            NativeUtils.inactive("");
        } else {
            NativeUtils.active("");
        }
    }

    @Inject(method = "closeImposedChat", at = @At("HEAD"))
    private void inactive(CallbackInfo ci) {
        NativeUtils.inactive("");
    }

    @WrapOperation(method = "clickMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiScreen;isCtrlKeyDown()Z", ordinal = 0))
    private boolean noAttackDump(Operation<Boolean> original) {
        return !ModernMiteConfig.NoAttackDump.getBooleanValue() && original.call();
    }
}
