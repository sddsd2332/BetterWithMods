package betterwithmods.common.container.bulk;

import betterwithmods.common.tile.TileCookingPot;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.container.ContainerTile;
import betterwithmods.library.utils.GuiUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public abstract class ContainerCookingPot<T extends TileCookingPot> extends ContainerTile<T> {

    public static final ResourceLocation HEAT = new ResourceLocation(ModLib.MODID, "heat");

    public ContainerCookingPot(T tile, EntityPlayer player) {
        super(tile, player);

        addBooleanProperty(GuiUtils.PROPERTY_SHOW_PROGRESS, () -> tile.getProgress() > 0);
        addIntProperty(GuiUtils.PROPERTY_PROGRESS, tile::getProgress);
        addIntProperty(GuiUtils.PROPERTY_MAX_PROGRESS, tile::getMax);
        addIntProperty(HEAT, () -> tile.getHeat(tile.getWorld(), tile.getPos()));
        GuiUtils.createPlayerSlots(player, this, 8, 111, 8, 169);
        GuiUtils.createContainerSlots(GuiUtils.SLOTS_CONTAINER_INVENTORY,this, tile.inventory, 27, 3, 0, 8, 43);
    }

}
