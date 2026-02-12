package moddedmite.modernmite.client.gui;

import fi.dy.masa.malilib.gui.GuiBase;
import moddedmite.modernmite.feat.ModChangeEntry;
import moddedmite.modernmite.feat.ModChangeReport;
import net.minecraft.GuiButton;
import net.minecraft.GuiScreen;
import net.minecraft.GuiSmallButton;

public class GuiModChangeWarning extends GuiScreen {
    private final ModChangeReport report;
    private final Runnable confirmAction;
    private final Runnable cancelAction;
    private GuiButton confirm;
    private GuiButton cancel;

    public GuiModChangeWarning(ModChangeReport report, Runnable confirmAction, Runnable cancelAction) {
        this.report = report;
        this.confirmAction = confirmAction;
        this.cancelAction = cancelAction;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.confirm = new GuiSmallButton(0, this.width / 2 - 155, this.height - 40, "仍然载入存档");
        this.cancel = new GuiSmallButton(1, this.width / 2 - 155 + 160, this.height - 40, "取消操作");
        this.buttonList.add(this.confirm);
        this.buttonList.add(this.cancel);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        this.drawDefaultBackground();
        super.drawScreen(par1, par2, par3);

        int centerX = this.width / 2;
        this.drawCenteredString(
                this.fontRenderer,
                "与上次游玩该存档时相比, 以下模组存在差异, 可能会导致物品和方块id错乱等问题.",
                centerX,
                20,
                GuiBase.COLOR_WHITE
        );
        this.drawCenteredString(
                this.fontRenderer,
                "建议你将模组恢复至原状, 或将存档备份后再操作.",
                centerX,
                30,
                GuiBase.COLOR_WHITE
        );
        int y = 50;
        for (ModChangeEntry entry : this.report.entries()) {
            this.drawCenteredString(
                    this.fontRenderer,
                    String.format("%s: %s", entry.getModId(), entry.getTip()),
                    centerX,
                    y,
                    GuiBase.COLOR_WHITE
            );
            y += 10;
            if (y > this.height) break;
        }
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton) {
        super.actionPerformed(par1GuiButton);
        switch (par1GuiButton.id) {
            case 0 -> this.confirmAction.run();
            case 1 -> this.cancelAction.run();
        }
    }
}
