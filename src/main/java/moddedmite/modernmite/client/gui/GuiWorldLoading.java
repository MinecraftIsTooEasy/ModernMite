package moddedmite.modernmite.client.gui;

import net.minecraft.GuiDownloadTerrain;
import net.minecraft.NetClientHandler;

public final class GuiWorldLoading extends GuiDownloadTerrain {

    public GuiWorldLoading(NetClientHandler netHandler) {
        super(netHandler);
    }

    @Override
    public void updateScreen() {
        // Minecraft.runTick processes pending packets without sending premature keepalives.
        // NetClientHandler.handleLogin replaces this screen with the normal terrain screen.
    }
}
