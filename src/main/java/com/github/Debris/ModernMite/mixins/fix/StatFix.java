package com.github.Debris.ModernMite.mixins.fix;

import net.minecraft.StatBase;
import net.minecraft.StatList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = StatList.class, priority = 999)
public abstract class StatFix {
    @Shadow public static StatBase minutesPlayedStat;

    @Overwrite
    public static boolean isEitherZeroOrOne(StatBase stat) {
        if (stat == null) return false;
        return stat.isAchievement();
    }

    @Overwrite
    public static boolean hasLongValue(StatBase stat) {
        if (stat == null) return false;
        return stat.getType() == StatBase.distanceStatType || stat == minutesPlayedStat;
    }

    @Inject(method = "replaceSimilarBlocks",  at = @At("HEAD"), cancellable = true)
    private static void fixStatNull_2(StatBase[] par0ArrayOfStatBase, int par1, int par2, CallbackInfo ci) {
        if (par0ArrayOfStatBase == null) ci.cancel();
    }
}
