package moddedmite.modernmite.mixins.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.modernmite.config.ModernMiteConfig;
import net.minecraft.Entity;
import net.minecraft.EntityArachnid;
import net.minecraft.RaycastCollision;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityArachnid.class)
public class EntityArachnidMixin {
    @WrapOperation(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/RaycastCollision;getEntityHit()Lnet/minecraft/Entity;"))
    private Entity fixNPE(RaycastCollision instance, Operation<Entity> original) {
        if (ModernMiteConfig.SpiderNPEFix.getBooleanValue() && instance == null) return null;
        return original.call(instance);
    }
}
