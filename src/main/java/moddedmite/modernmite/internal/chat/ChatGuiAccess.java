package moddedmite.modernmite.internal.chat;

import net.minecraft.ChatMessageComponent;
import org.jetbrains.annotations.Nullable;

public interface ChatGuiAccess {

    void modernMite$printChatMessage(ChatMessageComponent component);

    @Nullable
    String modernMite$getOpenUrl(int mouseX, int mouseY);
}
