package moddedmite.modernmite.internal.chat;

import net.minecraft.ChatMessageComponent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ChatLineAccess {

    void modernMite$setLinks(List<ChatLinkSpan> links);

    void modernMite$setSourceComponent(@Nullable ChatMessageComponent component);

    @Nullable
    ChatMessageComponent modernMite$getSourceComponent();

    @Nullable
    ChatLinkSpan modernMite$getLink(int characterIndex);
}
