package moddedmite.modernmite.mixins.client.gui;

import moddedmite.modernmite.client.gui.GuiModChangeWarning;
import moddedmite.modernmite.feat.ModChangeReport;
import moddedmite.modernmite.feat.ModChangeWarning;
import net.minecraft.GuiScreen;
import net.minecraft.GuiSelectWorld;
import net.minecraft.SaveFormatComparator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiSelectWorld.class)
public abstract class GuiSelectWorldMixin extends GuiScreen {
    @Shadow
    private List<?> saveList;

    @Shadow
    public abstract void selectWorld(int par1);

    @Inject(method = "selectWorld", at = @At("HEAD"), cancellable = true)
    private void checkMod(int par1, CallbackInfo ci) {
        if (!ModChangeWarning.isActive()) return;

        SaveFormatComparator saveInfo = (SaveFormatComparator) this.saveList.get(par1);

        ModChangeReport report = ModChangeWarning.generateReport(this.mc, saveInfo);
        if (report.pass()) return;

        this.mc.displayGuiScreen(new GuiModChangeWarning(
                report,
                () -> {
                    ModChangeWarning.setActive(false);
                    this.selectWorld(par1);
                    ModChangeWarning.setActive(true);
                },
                () -> this.mc.displayGuiScreen(this)
        ));
        ci.cancel();
    }
}
