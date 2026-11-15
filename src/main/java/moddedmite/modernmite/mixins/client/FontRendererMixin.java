package moddedmite.modernmite.mixins.client;

import net.minecraft.FontRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FontRenderer.class)
public class FontRendererMixin {

    @Shadow
    private byte[] glyphWidth;

    @ModifyVariable(method = "renderUnicodeChar(CZ)F", at = @At("STORE"), name = "var4")
    private int decodeGlyphLeftEdge(int var4) {
        return var4 & 15;
    }

    @Inject(method = "getCharWidth(C)I", at = @At(value = "FIELD", target = "Lnet/minecraft/FontRenderer;glyphWidth:[B", ordinal = 0, opcode = Opcodes.GETFIELD), cancellable = true)
    private void measureUnicodeGlyph(char par1, CallbackInfoReturnable<Integer> cir) {
        int packed = this.glyphWidth[par1] & 255;
        cir.setReturnValue(packed == 0 ? 0 : ((packed & 15) + 1 - (packed >>> 4)) / 2 + 1);
    }
}
