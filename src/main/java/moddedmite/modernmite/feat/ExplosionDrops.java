package moddedmite.modernmite.feat;

import net.minecraft.BlockAnvil;
import net.minecraft.BlockBreakInfo;
import net.minecraft.ItemStack;
import net.minecraft.TileEntityAnvil;

public class ExplosionDrops {

    public static BlockBreakInfo prepareAnvilDrop(BlockBreakInfo info, ItemStack stack) {
        if (!(info.block instanceof BlockAnvil)) {
            return info;
        }

        // A dropped anvil has no world tile entity; its drop state belongs to the stack.
        TileEntityAnvil anvil = new TileEntityAnvil();
        anvil.damage = stack.getItemDamage();
        if (stack.hasDisplayName()) {
            anvil.setCustomInvName(stack.getDisplayName());
        }
        info.tile_entity = anvil;
        return info.setMetadata(stack.getItemAsBlock().getMetadata(stack.getItemSubtype()));
    }
}
