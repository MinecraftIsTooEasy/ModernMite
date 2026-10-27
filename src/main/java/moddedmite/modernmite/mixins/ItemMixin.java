package moddedmite.modernmite.mixins;

import net.minecraft.Item;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Inject(method = "getUnlocalizedName(Lnet/minecraft/ItemStack;)Ljava/lang/String;", at = @At("RETURN"), cancellable = true)
    private void normalizeFishName(ItemStack par1ItemStack, CallbackInfoReturnable<String> cir) {
        Item item = (Item) (Object) this;
        if (item == Item.fishRaw) {
            cir.setReturnValue("item.fishCodRaw");
        } else if (item == Item.fishCooked) {
            cir.setReturnValue("item.fishCodCooked");
        } else if (item == Item.fishLargeRaw) {
            cir.setReturnValue("item.fishSalmonRaw");
        } else if (item == Item.fishLargeCooked) {
            cir.setReturnValue("item.fishSalmonCooked");
        }
    }
}
