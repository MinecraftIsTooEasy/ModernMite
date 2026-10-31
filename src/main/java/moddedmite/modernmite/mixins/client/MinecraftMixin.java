package moddedmite.modernmite.mixins.client;

import moddedmite.modernmite.config.ModernMiteConfig;
import com.github.skystardust.InputMethodBlocker.NativeUtils;
import com.github.skystardust.InputMethodBlocker.compat.InputMethodHandler;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.EntityClientPlayerMP;
import net.minecraft.EntityPlayer;
import net.minecraft.GameSettings;
import net.minecraft.GuiChat;
import net.minecraft.GuiDownloadTerrain;
import net.minecraft.GuiScreen;
import net.minecraft.INetworkManager;
import net.minecraft.Minecraft;
import net.minecraft.NetClientHandler;
import net.minecraft.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    public EntityClientPlayerMP thePlayer;
    @Shadow
    public GameSettings gameSettings;

    @Shadow
    public GuiScreen currentScreen;

    @Shadow
    public abstract void displayGuiScreen(GuiScreen par1GuiScreen);

    @WrapWithCondition(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/ReferenceFileWriter;write()V"))
    private boolean doNotWriteReferenceFile() {
        return !ModernMiteConfig.NoReferenceFile.getBooleanValue();
    }

    @WrapOperation(method = "launchIntegratedServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/NetClientHandler;getNetManager()Lnet/minecraft/INetworkManager;"))
    private INetworkManager showWorldLoadingScreen(NetClientHandler handler, Operation<INetworkManager> original) {
        INetworkManager networkManager = original.call(handler);
        if (ModernMiteConfig.WorldLoadingInputFix.getBooleanValue()) {
            // Keep queued loading clicks away from the main menu before the login packet arrives.
            this.displayGuiScreen(new GuiDownloadTerrain(handler));
        }
        return networkManager;
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

    @WrapOperation(method = "runTick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;isKeyDown(I)Z"), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/GameSettings;keyBindChat:Lnet/minecraft/KeyBinding;"), to = @At(value = "INVOKE", target = "Lnet/minecraft/PlayerControllerMP;inAutoUseMode()Z", ordinal = 0)), require = 2, allow = 2)
    private boolean useLegacyChatOpening(int keyCode, Operation<Boolean> original) {
        return !ModernMiteConfig.VanillaChat.getBooleanValue() && original.call(keyCode);
    }

    @Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/PlayerControllerMP;curBlockDamageMP:F", ordinal = 0), slice = @Slice(from = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;next()Z")))
    private void openChatAfterKeyboardInput(CallbackInfo ci) {
        if (!ModernMiteConfig.VanillaChat.getBooleanValue() || this.thePlayer == null || this.currentScreen != null) return;

        // Consume opening keys after keyboard dispatch, as in vanilla handleKeybinds.
        boolean canOpenChat = this.gameSettings.chatVisibility != 2 && !GuiScreen.isCtrlKeyDown();
        while (this.gameSettings.keyBindChat.isPressed()) {
            if (canOpenChat) {
                this.displayGuiScreen(new GuiChat(Minecraft.last_aborted_chat));
            }
        }
        if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed() && canOpenChat) {
            this.displayGuiScreen(new GuiChat("/"));
        }
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
}
