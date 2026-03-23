package moddedmite.modernmite.mixins.entity;

import moddedmite.modernmite.config.ModernMiteConfig;
import net.minecraft.EntityItem;
import net.minecraft.Item;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityItem.class)
public abstract class EntityItemMixin {
    @Shadow
    public abstract ItemStack getEntityItem();

    @Inject(method = "isImmuneToExplosion", at = @At("HEAD"), cancellable = true)
    private void fixExplosionImmune(CallbackInfoReturnable<Boolean> cir) {
        if (ModernMiteConfig.NetherStarFix.getBooleanValue()) {
            ItemStack stack = this.getEntityItem();
            if (stack != null && stack.getItem() == Item.netherStar) {
                cir.setReturnValue(true);
            }
        }
    }
}
