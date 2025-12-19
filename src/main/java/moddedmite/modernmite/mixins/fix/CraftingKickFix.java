package moddedmite.modernmite.mixins.fix;

import moddedmite.modernmite.ModernMite;
import moddedmite.modernmite.config.ModernMiteConfig;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlotCrafting.class)
public abstract class CraftingKickFix extends Slot {
    @Shadow
    public CraftingResult crafting_result;

    @Shadow
    private EntityPlayer thePlayer;

    public CraftingKickFix(IInventory inventory, int slot_index, int display_x, int display_y) {
        super(inventory, slot_index, display_x, display_y);
    }

    @Inject(method = "onPickupFromSlot", at = @At("HEAD"), cancellable = true)
    private void fixKick(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack, CallbackInfo ci) {
        if (this.crafting_result == null && ModernMiteConfig.CraftingKickFix.getBooleanValue()) {
            ModernMite.LOGGER.warn("slot crafting fix: crafting result is null:\nworld {}\ncontainer {}\nplayer {}", this.getContainer().world, this.getContainer(), thePlayer);
            ci.cancel();
        }
    }
}
