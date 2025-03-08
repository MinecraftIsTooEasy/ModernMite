package com.github.Debris.ModernMite.mixins.render;

import com.github.Debris.ModernMite.config.ModernMiteConfig;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(FontRenderer.class)
public abstract class FontRendererMixin {
    @Unique
    private final int[] colorCode = new int[32];
    @Shadow
    private float red;
    @Shadow
    private float blue;
    @Shadow
    private float green;
    @Shadow
    private float alpha;
    @Shadow
    private boolean randomStyle;
    @Shadow
    private boolean boldStyle;
    @Shadow
    private boolean strikethroughStyle;
    @Shadow
    private boolean underlineStyle;
    @Shadow
    private boolean italicStyle;
    @Shadow
    private int textColor;
    @Shadow
    public Random fontRandom;
    @Shadow
    private int[] charWidth;
    @Shadow
    private float posX;
    @Shadow
    private float posY;
    @Shadow
    public int FONT_HEIGHT;
    @Shadow
    private boolean bidiFlag;
    @Shadow
    private byte[] glyphWidth;
    @Unique
    private final String ASCII = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000";

    public FontRendererMixin() {
    }

    @Shadow
    protected abstract String bidiReorder(String var1);

    @Shadow
    protected abstract float renderDefaultChar(int var1, boolean var2);

    @Shadow
    protected abstract float renderUnicodeChar(char var1, boolean var2);

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void setColor(GameSettings par1GameSettings, ResourceLocation par2ResourceLocation, TextureManager par3TextureManager, boolean par4, CallbackInfo ci) {
        for (int var5 = 0; var5 < 32; ++var5) {
            int var6 = (var5 >> 3 & 1) * 85;
            int var7 = (var5 >> 2 & 1) * 170 + var6;
            int var8 = (var5 >> 1 & 1) * 170 + var6;
            int var9 = (var5 >> 0 & 1) * 170 + var6;
            if (var5 == 6) {
                var7 += 85;
            }

            if (par1GameSettings.anaglyph) {
                int var10 = (var7 * 30 + var8 * 59 + var9 * 11) / 100;
                int var11 = (var7 * 30 + var8 * 70) / 100;
                int var12 = (var7 * 30 + var9 * 70) / 100;
                var7 = var10;
                var8 = var11;
                var9 = var12;
            }

            if (var5 >= 16) {
                var7 /= 4;
                var8 /= 4;
                var9 /= 4;
            }

            this.colorCode[var5] = (var7 & 255) << 16 | (var8 & 255) << 8 | var9 & 255;
        }

    }

    @Inject(
            method = "drawString(Ljava/lang/String;IIIZ)I",
            at = @At("HEAD")
    )
    private void alpha_test(String par1Str, int par2, int par3, int par4, boolean par5, CallbackInfoReturnable<Integer> cir) {
        GL11.glEnable(3008);
    }

    @Inject(
            method = "getCharWidth",
            at = @At("HEAD"),
            cancellable = true
    )
    private void newWidth(char par1, CallbackInfoReturnable<Integer> cir) {
        if (par1 == 167) {
            cir.setReturnValue(-1);
        } else if (par1 == ' ') {
            cir.setReturnValue(4);
        } else {
            int var2 = this.ASCII.indexOf(par1);
            if (par1 > 0 && var2 != -1 && ModernMiteConfig.ASCIIFont.getBooleanValue()) {
                cir.setReturnValue(this.charWidth[var2]);
            } else if (this.glyphWidth[par1] != 0) {
                int var3 = this.glyphWidth[par1] >>> 4;
                int var4 = this.glyphWidth[par1] & 15;
                if (var4 > 7) {
                    var4 = 15;
                    var3 = 0;
                }

                ++var4;
                cir.setReturnValue((var4 - var3) / 2 + 1);
            } else {
                cir.setReturnValue(0);
            }
        }

    }

    @Inject(
            method = "renderStringAtPos",
            at = @At("HEAD"),
            cancellable = true
    )
    private void newRender(String string, boolean shadow, CallbackInfo ci) {
        if (!ModernMiteConfig.ASCIIFont.getBooleanValue()) return;
        ci.cancel();

        for (int var3 = 0; var3 < string.length(); ++var3) {
            char var4 = string.charAt(var3);
            int var5;
            int var6;
            if (var4 == 167 && var3 + 1 < string.length()) {
                var5 = "0123456789abcdefklmnor".indexOf(string.toLowerCase().charAt(var3 + 1));
                if (var5 < 16) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (var5 < 0 || var5 > 15) {
                        var5 = 15;
                    }

                    if (shadow) {
                        var5 += 16;
                    }

                    var6 = this.colorCode[var5];
                    this.textColor = var6;
                    GL11.glColor4f((float) (var6 >> 16) / 255.0F, (float) (var6 >> 8 & 255) / 255.0F, (float) (var6 & 255) / 255.0F, this.alpha);
                } else if (var5 == 16) {
                    this.randomStyle = true;
                } else if (var5 == 17) {
                    this.boldStyle = true;
                } else if (var5 == 18) {
                    this.strikethroughStyle = true;
                } else if (var5 == 19) {
                    this.underlineStyle = true;
                } else if (var5 == 20) {
                    this.italicStyle = true;
                } else if (var5 == 21) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    GL11.glColor4f(this.red, this.blue, this.green, this.alpha);
                }

                ++var3;
            } else {
                var5 = this.ASCII.indexOf(var4);
                if (this.randomStyle && var5 != -1) {
                    do {
                        var6 = this.fontRandom.nextInt(this.charWidth.length);
                    } while (this.charWidth[var5] != this.charWidth[var6]);

                    var5 = var6;
                }

                float var11 = var5 == -1 ? 0.5F : 1.0F;
                boolean var7 = (var4 == 0 || var5 == -1) && shadow;
                if (var7) {
                    this.posX -= var11;
                    this.posY -= var11;
                }

                float var8 = this.newRenderChar(var5, var4, this.italicStyle);
                if (var7) {
                    this.posX += var11;
                    this.posY += var11;
                }

                if (this.boldStyle) {
                    this.posX += var11;
                    if (var7) {
                        this.posX -= var11;
                        this.posY -= var11;
                    }

                    this.newRenderChar(var5, var4, this.italicStyle);
                    this.posX -= var11;
                    if (var7) {
                        this.posX += var11;
                        this.posY += var11;
                    }

                    ++var8;
                }

                Tessellator var9;
                if (this.strikethroughStyle) {
                    var9 = Tessellator.instance;
                    GL11.glDisable(3553);
                    var9.startDrawingQuads();
                    var9.addVertex(this.posX, this.posY + (float) (this.FONT_HEIGHT / 2), 0.0);
                    var9.addVertex(this.posX + var8, this.posY + (float) (this.FONT_HEIGHT / 2), 0.0);
                    var9.addVertex(this.posX + var8, this.posY + (float) (this.FONT_HEIGHT / 2) - 1.0F, 0.0);
                    var9.addVertex(this.posX, this.posY + (float) (this.FONT_HEIGHT / 2) - 1.0F, 0.0);
                    var9.draw();
                    GL11.glEnable(3553);
                }

                if (this.underlineStyle) {
                    var9 = Tessellator.instance;
                    GL11.glDisable(3553);
                    var9.startDrawingQuads();
                    int var10 = this.underlineStyle ? -1 : 0;
                    var9.addVertex(this.posX + (float) var10, this.posY + (float) this.FONT_HEIGHT, 0.0);
                    var9.addVertex(this.posX + var8, this.posY + (float) this.FONT_HEIGHT, 0.0);
                    var9.addVertex(this.posX + var8, this.posY + (float) this.FONT_HEIGHT - 1.0F, 0.0);
                    var9.addVertex(this.posX + (float) var10, this.posY + (float) this.FONT_HEIGHT - 1.0F, 0.0);
                    var9.draw();
                    GL11.glEnable(3553);
                }

                this.posX += (float) ((int) var8);
            }
        }

    }

    @ModifyVariable(
            method = "renderString",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private String bidi(String par1Str) {
        if (par1Str != null && this.bidiFlag) {
            par1Str = this.bidiReorder(par1Str);
        }

        return par1Str;
    }

    @Unique
    private float newRenderChar(int par1, char par2, boolean par3) {
        if (par2 == ' ') {
            return 4.0F;
        } else {
            return (this.ASCII.indexOf(par2) != -1 && ModernMiteConfig.ASCIIFont.getBooleanValue()) ? this.renderDefaultChar(par1, par3) : this.renderUnicodeChar(par2, par3);
        }
    }
}
