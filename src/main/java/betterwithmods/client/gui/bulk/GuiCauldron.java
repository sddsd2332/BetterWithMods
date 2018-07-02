package betterwithmods.client.gui.bulk;

import betterwithmods.common.blocks.mechanical.tile.TileCauldron;
import net.minecraft.entity.player.EntityPlayer;

public class GuiCauldron extends GuiCookingPot {
    public GuiCauldron(EntityPlayer player, TileCauldron cauldron) {
        super(player, cauldron);
    }
}
