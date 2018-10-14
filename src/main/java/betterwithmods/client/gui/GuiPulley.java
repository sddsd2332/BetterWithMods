package betterwithmods.client.gui;

import betterwithmods.common.container.other.ContainerPulley;
import betterwithmods.common.tile.TilePulley;
import betterwithmods.lib.ModLib;
import betterwithmods.library.client.gui.GuiBase;
import betterwithmods.library.client.gui.GuiProgress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiPulley extends GuiBase<ContainerPulley> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModLib.MODID, "textures/gui/pulley.png");

    public GuiPulley(ContainerPulley container) {
        super(container, TEXTURE);
        this.ySize = 193;
    }

//    private final TilePulley tile;
//
//    public GuiPulley(EntityPlayer player, TilePulley tile) {
//        super(new ContainerPulley(player, tile), TEXTURE);
//        this.ySize = 193;
//        this.tile = tile;
//    }

    @Override
    public String getTitle() {
        return getContainer().getTile().getName();
    }

    @Override
    public int getTitleY() {
        return 6;
    }

//    @Override
//    public int getX() {
//        return 81;
//    }
//
//    @Override
//    public int getY() {
//        return 30;
//    }
//
//    @Override
//    public int getTextureX() {
//        return 176;
//    }
//
//    @Override
//    public int getTextureY() {
//        return 14;
//    }
//
//    @Override
//    public int getHeight() {
//        return 14;
//    }
//
//    @Override
//    public int getWidth() {
//        return 14;
//    }
//
//    @Override
//    protected int toPixels() {
//        return (int) (getHeight() * getPercentage());
//    }
}
