package moddedmite.modernmite.mixins.fix;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.StatBase;
import net.minecraft.StatList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatList.class)
public abstract class StatFix {
    @ModifyReturnValue(method = "isEitherZeroOrOne", at = @At("TAIL"))
    private static boolean fixStatNull_0(boolean original, @Local(argsOnly = true) StatBase stat) {
        if (stat != null) return original;
        return false;
    }

    @ModifyReturnValue(method = "hasLongValue", at = @At("TAIL"))
    private static boolean fixStatNull_1(boolean original, @Local(argsOnly = true) StatBase stat) {
        if (stat != null) return original;
        return false;
    }

    @Inject(method = "replaceSimilarBlocks",  at = @At("HEAD"), cancellable = true)
    private static void fixStatNull_2(StatBase[] par0ArrayOfStatBase, int par1, int par2, CallbackInfo ci) {
        if (par0ArrayOfStatBase == null) ci.cancel();
    }
}
