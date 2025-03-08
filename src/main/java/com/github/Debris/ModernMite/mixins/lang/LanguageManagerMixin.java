package com.github.Debris.ModernMite.mixins.lang;

import com.google.common.collect.Lists;
import net.minecraft.LanguageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;

@Mixin(LanguageManager.class)
public class LanguageManagerMixin {
    @Shadow
    private String currentLanguage;

    @ModifyArg(method = "onResourceManagerReload", at = @At(value = "INVOKE", target = "Lnet/minecraft/Locale;loadLocaleDataFiles(Lnet/minecraft/ResourceManager;Ljava/util/List;)V"), index = 1)
    private List inject(List list) {
        ArrayList<String> languageList = Lists.newArrayList("MITE", "en_US");
        if (!"en_US".equals(this.currentLanguage)) {
            languageList.add(this.currentLanguage);
        }
        return languageList;
    }
}
