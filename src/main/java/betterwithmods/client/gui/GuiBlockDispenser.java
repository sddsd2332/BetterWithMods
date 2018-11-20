package betterwithmods.client.gui;

import betterwithmods.common.container.other.ContainerAdvancedDispenser;
import betterwithmods.lib.ModLib;
import betterwithmods.library.client.gui.GuiBase;
import net.minecraft.util.ResourceLocation;

public class GuiBlockDispenser extends GuiBase<ContainerAdvancedDispenser> {
    private static final int guiHeight = 182;

    public GuiBlockDispenser(ContainerAdvancedDispenser container) {
        super(container, new ResourceLocation(ModLib.MODID, "textures/gui/dispenser.png"));
        this.ySize = guiHeight;
    }

    @Override
    public int getTitleY() {
        return 6;
    }


    @Override
    protected void drawExtras(float partialTicks, int mouseX, int mouseY, int centerX, int centerY) {
        int xOff = getContainer().getTile().getNextIndex() % 4 * 18;
        int yOff = getContainer().getTile().getNextIndex() / 4 * 18;
        drawTexturedModalRect(centerX + 51 + xOff, centerY + 15 + yOff, 176, 0, 20, 20);
    }
}
