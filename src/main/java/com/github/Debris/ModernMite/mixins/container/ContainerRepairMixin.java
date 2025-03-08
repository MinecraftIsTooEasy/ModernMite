package com.github.Debris.ModernMite.mixins.container;

import com.github.Debris.ModernMite.config.ModernMiteConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.ContainerRepair;
import net.minecraft.EntityItem;
import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ContainerRepair.class)
public class ContainerRepairMixin {
    @ModifyExpressionValue(method = "onContainerClosed", at = @At(value = "FIELD", target = "Lnet/minecraft/World;isRemote:Z", opcode = Opcodes.GETFIELD))
    private boolean backToInventory(boolean original) {
        if (ModernMiteConfig.BetterContainerQuitting.getBooleanValue()) {
            return false;
        }
        return original;
    }

    @Redirect(method = "onContainerClosed", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityPlayer;dropPlayerItem(Lnet/minecraft/ItemStack;)Lnet/minecraft/EntityItem;"))
    private EntityItem backToInventory(EntityPlayer instance, ItemStack par1ItemStack) {
        if (ModernMiteConfig.BetterContainerQuitting.getBooleanValue() && instance.inventory.addItemStackToInventory(par1ItemStack)) {
            return null;
        } else {
            return instance.dropPlayerItem(par1ItemStack);
        }
    }
}
