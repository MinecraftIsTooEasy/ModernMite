package moddedmite.modernmite.mixins.client.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.modernmite.internal.chat.ChatGuiAccess;
import moddedmite.modernmite.internal.chat.ChatLineAccess;
import moddedmite.modernmite.internal.chat.ChatLinkHit;
import moddedmite.modernmite.internal.chat.ChatLinkSpan;
import moddedmite.modernmite.internal.chat.ChatLinkSupport;
import moddedmite.modernmite.internal.chat.ChatTextSupport;
import net.minecraft.ChatLine;
import net.minecraft.ChatMessageComponent;
import net.minecraft.GuiNewChat;
import net.minecraft.MathHelper;
import net.minecraft.Minecraft;
import net.minecraft.ScaledResolution;
import net.minecraft.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.Deque;
import java.util.List;

@Mixin(GuiNewChat.class)
public abstract class GuiNewChatMixin implements ChatGuiAccess {

    @Shadow
    @Final
    private Minecraft mc;

    @Shadow
    @Final
    private List<ChatLine> field_96134_d;

    @Shadow
    private int field_73768_d;

    @Shadow
    public abstract int func_96126_f();

    @Shadow
    public abstract int func_96127_i();

    @Shadow
    public abstract float func_96131_h();

    @Shadow
    public abstract boolean getChatOpen();

    @Shadow
    public abstract void printChatMessage(String message);

    @Unique
    private Deque<List<ChatLinkSpan>> modernMite$pendingLineLinks;

    @Unique
    private ChatMessageComponent modernMite$pendingSourceComponent;

    @Override
    public void modernMite$printChatMessage(ChatMessageComponent component) {
        String formattedMessage = component.toStringWithFormatting(true);
        int width = MathHelper.floor_float((float) this.func_96126_f() / this.func_96131_h());
        List<?> wrappedLines = this.mc.fontRenderer.listFormattedStringToWidth(formattedMessage, width);
        this.modernMite$pendingLineLinks = ChatLinkSupport.createWrappedLineLinks(component, wrappedLines);
        this.modernMite$pendingSourceComponent = component;
        try {
            this.printChatMessage(formattedMessage);
        } finally {
            this.modernMite$pendingLineLinks = null;
            this.modernMite$pendingSourceComponent = null;
        }
    }

    @WrapOperation(method = "func_96129_a", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V", ordinal = 0))
    private void modernMite$attachLinks(List<?> instance, int index, Object value, Operation<Void> original) {
        if (value instanceof ChatLine chatLine) {
            List<ChatLinkSpan> links;
            if (this.modernMite$pendingLineLinks != null) {
                links = this.modernMite$pendingLineLinks.isEmpty()
                        ? Collections.emptyList()
                        : this.modernMite$pendingLineLinks.removeFirst();
            } else {
                links = ChatLinkSupport.findPlainTextLinks(chatLine.getChatLineString());
            }
            ((ChatLineAccess) chatLine).modernMite$setLinks(links);
        }
        original.call(instance, index, value);
    }

    @WrapOperation(method = "func_96129_a", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V", ordinal = 1))
    private void modernMite$preserveSourceComponent(List<?> instance, int index, Object value, Operation<Void> original) {
        if (value instanceof ChatLine chatLine && this.modernMite$pendingSourceComponent != null) {
            ((ChatLineAccess) chatLine).modernMite$setSourceComponent(this.modernMite$pendingSourceComponent);
        }
        original.call(instance, index, value);
    }

    @WrapOperation(method = "func_96132_b", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"))
    private Object modernMite$prepareLinksForRefresh(List<?> instance, int index, Operation<Object> original) {
        Object value = original.call(instance, index);
        if (value instanceof ChatLine chatLine) {
            ChatMessageComponent component = ((ChatLineAccess) chatLine).modernMite$getSourceComponent();
            if (component != null) {
                int width = MathHelper.floor_float((float) this.func_96126_f() / this.func_96131_h());
                List<?> wrappedLines = this.mc.fontRenderer.listFormattedStringToWidth(chatLine.getChatLineString(), width);
                this.modernMite$pendingLineLinks = ChatLinkSupport.createWrappedLineLinks(component, wrappedLines);
            } else {
                this.modernMite$pendingLineLinks = null;
            }
        }
        return value;
    }

    @Inject(method = "func_96132_b", at = @At("RETURN"))
    private void modernMite$clearRefreshState(CallbackInfo ci) {
        this.modernMite$pendingLineLinks = null;
    }

    @Inject(method = "drawChat", at = @At("RETURN"))
    private void modernMite$highlightHoveredLink(int updateCounter, CallbackInfo ci) {
        if (!this.getChatOpen() || this.mc.gameSettings.gui_mode != 0) {
            return;
        }

        ScaledResolution resolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        int mouseX = Mouse.getX() * resolution.getScaledWidth() / this.mc.displayWidth;
        int mouseY = resolution.getScaledHeight() - Mouse.getY() * resolution.getScaledHeight() / this.mc.displayHeight - 1;
        ChatLinkHit hit = this.modernMite$getLinkHit(mouseX, mouseY);
        if (hit == null) {
            return;
        }

        ChatLine line = this.field_96134_d.get(hit.lineIndex());
        String text = line.getChatLineString();
        int rawStart = ChatTextSupport.rawIndex(text, hit.span().start());
        int rawEnd = ChatTextSupport.rawIndex(text, hit.span().end());
        int x = this.mc.fontRenderer.getStringWidth(text.substring(0, rawStart));
        String linkText = text.substring(rawStart, rawEnd);
        int leadingFormattingLength = ChatTextSupport.leadingFormattingLength(linkText);
        String highlighted = linkText.substring(0, leadingFormattingLength)
                + ChatTextSupport.FORMAT_CODE + "n" + linkText.substring(leadingFormattingLength);
        int displayLine = hit.lineIndex() - this.field_73768_d;

        GL11.glPushMatrix();
        GL11.glTranslatef(2.0F, 20.0F, 0.0F);
        float chatScale = this.func_96131_h();
        GL11.glScalef(chatScale, chatScale, 1.0F);
        this.mc.fontRenderer.drawStringWithShadow(highlighted, x, -displayLine * 9 - 26, 0xFFFFFFFF);
        GL11.glPopMatrix();
    }

    @Override
    @Nullable
    public String modernMite$getOpenUrl(int mouseX, int mouseY) {
        ChatLinkHit hit = this.modernMite$getLinkHit(mouseX, mouseY);
        return hit == null ? null : hit.span().url();
    }

    @Unique
    @Nullable
    private ChatLinkHit modernMite$getLinkHit(int mouseX, int mouseY) {
        ScaledResolution resolution = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        float chatScale = this.func_96131_h();
        int chatX = MathHelper.floor_float((mouseX - 2.0F) / chatScale);
        float mouseYFromBottom = resolution.getScaledHeight() - mouseY;
        int chatY = MathHelper.floor_float((mouseYFromBottom - 28.0F) / chatScale - 18.0F);
        if (chatX < 0 || chatY < 0) {
            return null;
        }

        int visibleLines = Math.min(this.func_96127_i(), this.field_96134_d.size());
        int maxWidth = MathHelper.floor_float((float) this.func_96126_f() / chatScale);
        if (chatX > maxWidth || chatY >= (this.mc.fontRenderer.FONT_HEIGHT + 1) * visibleLines) {
            return null;
        }

        int lineIndex = chatY / (this.mc.fontRenderer.FONT_HEIGHT + 1) + this.field_73768_d;
        if (lineIndex < 0 || lineIndex >= this.field_96134_d.size()) {
            return null;
        }
        ChatLine line = this.field_96134_d.get(lineIndex);
        String prefix = this.mc.fontRenderer.trimStringToWidth(line.getChatLineString(), chatX);
        String plainPrefix = StringUtils.stripControlCodes(prefix);
        int characterIndex = plainPrefix == null ? 0 : plainPrefix.length();
        ChatLinkSpan span = ((ChatLineAccess) line).modernMite$getLink(characterIndex);
        return span == null ? null : new ChatLinkHit(lineIndex, span);
    }

}
