package betterwithmods.client.gui.bulk;

import betterwithmods.client.gui.GuiProgress;
import betterwithmods.common.container.bulk.ContainerFilteredHopper;
import betterwithmods.common.tile.TileFilteredHopper;
import betterwithmods.lib.ModLib;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiFilteredHopper extends GuiProgress {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModLib.MODID, "textures/gui/hopper.png");
    private final TileFilteredHopper tile;

    public GuiFilteredHopper(EntityPlayer player, TileFilteredHopper tile) {
        super(new ContainerFilteredHopper(player, tile), TEXTURE);
        this.ySize = 193;
        this.tile = tile;
    }

    @Override
    public String getTitle() {
        return tile.getName();
    }

    @Override
    public int getTitleY() {
        return 6;
    }

    @Override
    public int getX() {
        return 80;
    }

    @Override
    public int getY() {
        return 18;
    }

    @Override
    public int getTextureX() {
        return 176;
    }

    @Override
    public int getTextureY() {
        return 14;
    }

    @Override
    public int getHeight() {
        return 14;
    }

    @Override
    public int getWidth() {
        return 14;
    }

    @Override
    protected int toPixels() {
        return (int) (getHeight() * getPercentage());
    }
}
