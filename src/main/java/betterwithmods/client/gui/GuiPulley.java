package betterwithmods.client.gui;

import betterwithmods.common.container.other.ContainerPulley;
import betterwithmods.lib.ModLib;
import betterwithmods.library.client.gui.GuiProgress;
import net.minecraft.util.ResourceLocation;

public class GuiPulley extends GuiProgress<ContainerPulley> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ModLib.MODID, "textures/gui/pulley.png");

    public GuiPulley(ContainerPulley container) {
        super(container, TEXTURE, 81, 30, 176, 14, 14, 14);
        this.ySize = 193;
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
