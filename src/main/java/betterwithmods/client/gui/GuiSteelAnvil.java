package betterwithmods.client.gui;

import betterwithmods.common.container.anvil.ContainerSteelAnvil;
import betterwithmods.lib.ModLib;
import betterwithmods.library.client.gui.GuiBase;
import net.minecraft.util.ResourceLocation;

public class GuiSteelAnvil extends GuiBase<ContainerSteelAnvil> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModLib.MODID, "textures/gui/steel_anvil.png");

    public GuiSteelAnvil(ContainerSteelAnvil container) {
        super(container, TEXTURE);
        this.ySize = 183;
    }

    @Override
    public String getTitle() {
        return getContainer().getTile().getName();
    }

    @Override
    public int getTitleY() {
        return 6;
    }
}