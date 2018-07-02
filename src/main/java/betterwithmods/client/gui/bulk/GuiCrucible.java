package betterwithmods.client.gui.bulk;

import betterwithmods.common.blocks.mechanical.tile.TileCrucible;
import net.minecraft.entity.player.EntityPlayer;

public class GuiCrucible extends GuiCookingPot {
    public GuiCrucible(EntityPlayer player, TileCrucible crucible) {
        super(player, crucible);
    }
}
