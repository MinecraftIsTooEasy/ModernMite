package moddedmite.modernmite.mixins.client.gui;

import moddedmite.modernmite.internal.chat.ChatGuiAccess;
import moddedmite.modernmite.internal.chat.ChatLinkSupport;
import net.minecraft.ChatClickData;
import net.minecraft.GuiChat;
import net.minecraft.GuiConfirmOpenLink;
import net.minecraft.GuiNewChat;
import net.minecraft.GuiScreen;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.URI;

@Mixin(GuiChat.class)
public abstract class GuiChatMixin extends GuiScreen {

    @Shadow
    private URI clickedURI;

    @Shadow
    private void func_73896_a(URI uri) {}

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void modernMite$openComponentUrl(int mouseX, int mouseY, int button, CallbackInfo ci) {
        if (button != 0 || !this.mc.gameSettings.chatLinks || Minecraft.getClientPlayer().inBed()) {
            return;
        }
        String url = ((ChatGuiAccess) this.mc.ingameGUI.getChatGUI()).modernMite$getOpenUrl(mouseX, mouseY);
        URI uri = ChatLinkSupport.parseHttpUrl(url);
        if (uri == null) {
            return;
        }

        if (this.mc.gameSettings.chatLinksPrompt) {
            this.clickedURI = uri;
            this.mc.displayGuiScreen(new GuiConfirmOpenLink((GuiChat) (Object) this, url, 0, false));
        } else {
            this.func_73896_a(uri);
        }
        ci.cancel();
    }

    @Redirect(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiNewChat;func_73766_a(II)Lnet/minecraft/ChatClickData;"))
    private ChatClickData modernMite$useStructuredLinkHitTesting(GuiNewChat chat, int mouseX, int mouseY) {
        return null;
    }
}
