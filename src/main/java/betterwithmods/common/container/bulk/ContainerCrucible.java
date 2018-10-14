package betterwithmods.common.container.bulk;

import betterwithmods.client.gui.bulk.GuiCrucible;
import betterwithmods.common.tile.TileCrucible;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCrucible extends ContainerCookingPot<TileCrucible> {
    public ContainerCrucible(TileCrucible tile, EntityPlayer player) {
        super(tile, player);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public GuiContainer createGui() {
        return new GuiCrucible(this);
    }

}
