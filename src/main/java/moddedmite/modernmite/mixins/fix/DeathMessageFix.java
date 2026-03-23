package moddedmite.modernmite.mixins.fix;

import moddedmite.modernmite.config.ModernMiteConfig;
import moddedmite.modernmite.feat.ComponentFactory;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityDamageSource.class)
public abstract class DeathMessageFix extends DamageSource {

    @Shadow
    private Entity responsible_entity;

    @Shadow
    private Entity immediate_entity;

    protected DeathMessageFix(String par1Str) {
        super(par1Str);
    }

    @Inject(method = "getDeathMessage", at = @At("HEAD"), cancellable = true)
    private void fix(EntityLivingBase par1EntityLivingBase, CallbackInfoReturnable<ChatMessageComponent> cir) {
        if (ModernMiteConfig.DeathMessageFix.getBooleanValue()) {
            Entity entity = this.responsible_entity == null ? this.immediate_entity : this.responsible_entity;
            ItemStack var2 = entity instanceof EntityLivingBase ? ((EntityLivingBase) entity).getHeldItemStack() : null;
            String var3 = "death.attack." + (this.is_hand_damage ? "hand_damage" : this.damageType);
            String var4 = var3 + ".item";
            ChatMessageComponent component;
            if (var2 != null && var2.hasDisplayName() && StatCollector.func_94522_b(var4))
                component = ChatMessageComponent.createFromTranslationWithSubstitutions(var4, ComponentFactory.of(par1EntityLivingBase), ComponentFactory.of(entity), var2.getDisplayName());
            else
                component = ChatMessageComponent.createFromTranslationWithSubstitutions(var3, ComponentFactory.of(par1EntityLivingBase), ComponentFactory.of(entity));

            cir.setReturnValue(component);
        }
    }
}
