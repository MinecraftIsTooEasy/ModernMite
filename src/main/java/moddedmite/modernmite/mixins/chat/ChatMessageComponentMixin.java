package moddedmite.modernmite.mixins.chat;

import moddedmite.modernmite.internal.chat.ChatLinkComponentAccess;
import net.minecraft.ChatMessageComponent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatMessageComponent.class)
public abstract class ChatMessageComponentMixin implements ChatLinkComponentAccess {

    @Unique
    private String modernMite$openUrl;

    @Shadow
    protected abstract String getText();

    @Shadow
    protected abstract String getTranslationKey();

    @Shadow
    protected abstract List<ChatMessageComponent> getSubComponents();

    @Inject(method = "<init>(Lnet/minecraft/ChatMessageComponent;)V", at = @At("RETURN"))
    private void modernMite$copyOpenUrl(ChatMessageComponent original, CallbackInfo ci) {
        this.modernMite$openUrl = ((ChatLinkComponentAccess) original).modernMite$getOpenUrl();
    }

    @Override
    @Nullable
    public String modernMite$getOpenUrl() {
        return this.modernMite$openUrl;
    }

    @Override
    public void modernMite$setOpenUrl(@Nullable String url) {
        this.modernMite$openUrl = url;
    }

    @Override
    public String modernMite$getText() {
        return this.getText();
    }

    @Override
    public String modernMite$getTranslationKey() {
        return this.getTranslationKey();
    }

    @Override
    public List<?> modernMite$getChildren() {
        return this.getSubComponents();
    }
}
