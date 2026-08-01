package moddedmite.modernmite.internal.chat;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ChatLinkComponentAccess {

    @Nullable
    String modernMite$getOpenUrl();

    void modernMite$setOpenUrl(@Nullable String url);

    @Nullable
    String modernMite$getText();

    @Nullable
    String modernMite$getTranslationKey();

    @Nullable
    List<?> modernMite$getChildren();
}
