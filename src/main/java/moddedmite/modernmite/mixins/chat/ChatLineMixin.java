package moddedmite.modernmite.mixins.chat;

import moddedmite.modernmite.internal.chat.ChatLineAccess;
import moddedmite.modernmite.internal.chat.ChatLinkSpan;
import net.minecraft.ChatLine;
import net.minecraft.ChatMessageComponent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Collections;
import java.util.List;

@Mixin(ChatLine.class)
public class ChatLineMixin implements ChatLineAccess {

    @Unique
    private List<ChatLinkSpan> modernMite$links = Collections.emptyList();

    @Unique
    private ChatMessageComponent modernMite$sourceComponent;

    @Override
    public void modernMite$setLinks(List<ChatLinkSpan> links) {
        this.modernMite$links = links;
    }

    @Override
    public void modernMite$setSourceComponent(@Nullable ChatMessageComponent component) {
        this.modernMite$sourceComponent = component;
    }

    @Override
    @Nullable
    public ChatMessageComponent modernMite$getSourceComponent() {
        return this.modernMite$sourceComponent;
    }

    @Override
    @Nullable
    public ChatLinkSpan modernMite$getLink(int characterIndex) {
        for (ChatLinkSpan link : this.modernMite$links) {
            if (characterIndex >= link.start() && characterIndex < link.end()) {
                return link;
            }
        }
        return null;
    }
}
