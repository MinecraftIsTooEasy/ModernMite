package com.github.Debris.ModernMite.mixins.fix;

import com.github.Debris.ModernMite.config.ModernMiteConfig;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPlayer.class)
public abstract class BoatFallDamageFix extends EntityLivingBase {
    public BoatFallDamageFix(World par1World) {
        super(par1World);
    }

    @WrapWithCondition(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityLivingBase;fall(F)V"))
    private boolean fixBoatFallDamage(EntityLivingBase instance, float cushioning) {
        if (ModernMiteConfig.BoatFallDamageFix.getBooleanValue() && ridingEntity instanceof EntityBoat) {
            super.fall(0);
            return false;
        }
        return true;
    }
}
