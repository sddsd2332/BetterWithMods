package betterwithmods.client.gui;

import betterwithmods.common.container.other.ContainerBlockDispenser;
import betterwithmods.common.tile.TileBlockDispenser;
import betterwithmods.lib.ModLib;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBlockDispenser extends GuiBase {
    private static final int guiHeight = 182;
    private static final String NAME = "inv.betterwithmods.dispenser.name";
    private final TileBlockDispenser tile;

    public GuiBlockDispenser(EntityPlayer player, TileBlockDispenser tile) {
        super(new ContainerBlockDispenser(player, tile), new ResourceLocation(ModLib.MODID, "textures/gui/dispenser.png"));
        this.tile = tile;
        this.ySize = guiHeight;
    }

    @Override
    public String getTitle() {
        return NAME;
    }

    @Override
    public int getTitleY() {
        return 6;
    }


    @Override
    protected void drawExtras(float partialTicks, int mouseX, int mouseY, int centerX, int centerY) {
        int xOff = this.tile.nextIndex % 4 * 18;
        int yOff = this.tile.nextIndex / 4 * 18;
        drawTexturedModalRect(centerX + 51 + xOff, centerY + 15 + yOff, 176, 0, 20, 20);
    }
}
