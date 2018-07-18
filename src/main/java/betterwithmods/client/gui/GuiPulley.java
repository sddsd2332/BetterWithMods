package betterwithmods.client.gui;

import betterwithmods.BWMod;
import betterwithmods.client.container.other.ContainerPulley;
import betterwithmods.common.tile.TilePulley;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiPulley extends GuiProgress {

    private static final ResourceLocation TEXTURE = new ResourceLocation(BWMod.MODID, "textures/gui/pulley.png");

    private final TilePulley tile;

    public GuiPulley(EntityPlayer player, TilePulley tile) {
        super(new ContainerPulley(player, tile), TEXTURE);
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
        return 81;
    }

    @Override
    public int getY() {
        return 30;
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
