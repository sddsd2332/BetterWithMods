package betterwithmods.client.gui;

import betterwithmods.common.container.anvil.ContainerSteelAnvil;
import betterwithmods.common.tile.TileSteelAnvil;
import betterwithmods.lib.ModLib;
import betterwithmods.library.client.gui.GuiBase;
import betterwithmods.library.common.container.ContainerBase;
import betterwithmods.library.common.container.ContainerTile;
import net.minecraft.util.ResourceLocation;

public class GuiSteelAnvil extends GuiBase<ContainerSteelAnvil> {

    private static final ResourceLocation tex = new ResourceLocation(ModLib.MODID, "textures/gui/steel_anvil.png");

    public GuiSteelAnvil(ContainerSteelAnvil container, ResourceLocation background) {
        super(container, background);
        this.ySize = 183;
    }

    @Override
    public String getTitle() {
        return getContainer().getTile().getName();
    }
//
//    public GuiSteelAnvil(TileSteelAnvil tileEntity, ContainerSteelAnvil container) {
//        super(container, tex);
//        this.ySize = 183;
//        tile = tileEntity;
//    }

    @Override
    public int getTitleY() {
        return 6;
    }
}