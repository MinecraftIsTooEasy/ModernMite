package com.github.Debris.ModernMite.compat;

import com.github.Debris.ModernMite.config.ModernMiteConfigEarly;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String s) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String s, String s1) {
        if (s.equals("net.minecraft.Packet")) {
            return ModernMiteConfigEarly.ReadStringFix.get();
        }
        if (s.equals("net.minecraft.NBTTagCompound")) {
            return ModernMiteConfigEarly.AchievementFix.get();
        }
        if (s.equals("net.minecraft.ServerPlayer")) {
            return ModernMiteConfigEarly.AchievementFix.get();
        }
        if (s.equals("net.minecraft.WorldInfoShared")) {
            return ModernMiteConfigEarly.AchievementFix.get();
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {

    }
}
