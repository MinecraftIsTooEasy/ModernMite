package moddedmite.modernmite.mixins.entity;

import net.minecraft.Entity;
import net.minecraft.EntityBoat;
import net.minecraft.EntitySquid;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityBoat.class)
public abstract class EntityBoatMixin extends Entity {
    public EntityBoatMixin(World par1World) {
        super(par1World);
    }

    @Inject(method = "applyEntityCollision", at = @At("HEAD"))
    private void inject(Entity entity, CallbackInfo ci) {
        if (this.onServer() && !this.isDead && entity instanceof EntitySquid) {
            System.out.println("collid with sqqqqqqqqqqqqqqqqqquid");
        }
    }

    @Inject(method = "applyEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityBoat;setDead()V"))
    private void print(Entity entity, CallbackInfo ci) {
        System.out.println("hit too much, the boat will die");
    }
}
