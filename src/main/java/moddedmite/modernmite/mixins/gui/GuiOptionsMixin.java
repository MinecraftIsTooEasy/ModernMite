package moddedmite.modernmite.mixins.gui;

import net.minecraft.GuiOptions;
import net.minecraft.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = GuiOptions.class, priority = 0)
public abstract class GuiOptionsMixin extends GuiScreen {
    @ModifyConstant(method = "initGui", constant = @Constant(intValue = 155), require = 0)
    private int align(int constant) {
        return 152;
    }

    @ModifyConstant(method = "initGui", constant = @Constant(intValue = 160), require = 0)
    private int align1(int constant) {
        return 154;
    }
}
