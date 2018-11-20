package betterwithmods.client.gui.bulk;

import betterwithmods.common.container.bulk.ContainerFilteredHopper;
import betterwithmods.lib.ModLib;
import betterwithmods.library.client.gui.GuiProgress;
import net.minecraft.util.ResourceLocation;

public class GuiFilteredHopper extends GuiProgress<ContainerFilteredHopper> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModLib.MODID, "textures/gui/hopper.png");

    public GuiFilteredHopper(ContainerFilteredHopper container) {
        super(container, TEXTURE, 80, 18, 176, 14, 14, 14);
        this.ySize = 193;
    }

    @Override
    public int getTitleY() {
        return 6;
    }

}
