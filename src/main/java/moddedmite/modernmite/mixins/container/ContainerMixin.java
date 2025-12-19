package moddedmite.modernmite.mixins.container;

import moddedmite.modernmite.config.ModernMiteConfig;
import net.minecraft.Container;
import net.minecraft.EntityItem;
import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Container.class)
public class ContainerMixin {
    @Redirect(method = "onContainerClosed", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityPlayer;dropPlayerItem(Lnet/minecraft/ItemStack;)Lnet/minecraft/EntityItem;"))
    private EntityItem backToInventory(EntityPlayer instance, ItemStack par1ItemStack) {
        if (ModernMiteConfig.BetterContainerQuitting.getBooleanValue() && instance.inventory.addItemStackToInventory(par1ItemStack)) {
            return null;
        } else {
            return instance.dropPlayerItem(par1ItemStack);
        }
    }
}
