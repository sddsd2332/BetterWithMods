package betterwithmods.common.container.bulk;

import betterwithmods.client.gui.bulk.GuiCauldron;
import betterwithmods.common.tile.TileCauldron;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCauldron extends ContainerCookingPot<TileCauldron> {
    public ContainerCauldron(TileCauldron tile, EntityPlayer player) {
        super(tile, player);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiContainer createGui() {
        return new GuiCauldron(this);
    }
}
