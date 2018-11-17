package betterwithmods.client.gui.bulk;

import betterwithmods.common.container.bulk.ContainerCookingPot;
import betterwithmods.lib.ModLib;
import betterwithmods.library.client.gui.GuiProgress;
import betterwithmods.library.common.container.ContainerBase;
import net.minecraft.util.ResourceLocation;

public class GuiCookingPot<C extends ContainerBase> extends GuiProgress<C> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(ModLib.MODID, "textures/gui/cooking_pot.png");


    public GuiCookingPot(C container) {
        super(container, TEXTURE, 81, 19, 176, 14, 14, 14);
        this.ySize = 193;
    }

    private int getHeat() {
        return getContainer().getPropertyValue(ContainerCookingPot.HEAT);
    }

    @Override
    public int getTextureX() {
        return super.getTextureX() + (getHeat() > 0 ? 0 : getWidth());
    }


    @Override
    public String getTitle() {
        return "inv.crucible.name";
    }

    @Override
    public int getTitleY() {
        return 6;
    }

}
