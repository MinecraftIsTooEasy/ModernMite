package moddedmite.modernmite.mixins;

import moddedmite.modernmite.feat.ModChangeWarning;
import net.minecraft.NBTTagCompound;
import net.minecraft.SaveHandler;
import net.minecraft.WorldInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(SaveHandler.class)
public class SaveHandlerMixin {
    @Shadow
    @Final
    private File worldDirectory;

    @Inject(method = "saveWorldInfoWithPlayer", at = @At(value = "INVOKE", target = "Ljava/io/FileWriter;close()V", shift = At.Shift.AFTER))
    private void injectModList(WorldInfo par1WorldInfo, NBTTagCompound par2NBTTagCompound, CallbackInfo ci) {
        ModChangeWarning.saveModList(this.worldDirectory);
    }
}
