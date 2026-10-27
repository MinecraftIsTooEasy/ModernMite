package moddedmite.modernmite.mixins;

import net.minecraft.BlockIce;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BlockIce.class)
public abstract class BlockIceMixin {

    @ModifyArg(method = "shouldSideBeRendered(Lnet/minecraft/IBlockAccess;IIII)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/BlockBreakable;shouldSideBeRendered(Lnet/minecraft/IBlockAccess;IIII)Z"), index = 4)
    private int restoreFaceSide(int side) {
        // BlockIce passes 1 - side to BlockBreakable; undo that conversion before face culling.
        return 1 - side;
    }
}
