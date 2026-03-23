package moddedmite.modernmite.feat;

import net.minecraft.ChatMessageComponent;
import net.minecraft.Entity;

public class ComponentFactory {
    public static ChatMessageComponent of(Entity entity) {
        return ChatMessageComponent.createFromTranslationKey(entity.getTranslatedEntityName());
    }
}
