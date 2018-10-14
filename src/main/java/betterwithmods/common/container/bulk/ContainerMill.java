package betterwithmods.common.container.bulk;

import betterwithmods.client.gui.bulk.GuiMill;
import betterwithmods.common.tile.TileMill;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.container.ContainerTile;
import betterwithmods.library.utils.GuiUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ContainerMill extends ContainerTile<TileMill> {

        public static ResourceLocation BLOCKED = new ResourceLocation(ModLib.MODID, "mill_blocked");

    public ContainerMill(TileMill tile, EntityPlayer player) {
        super(tile, player);

        addBooleanProperty(BLOCKED, tile::isBlocked);
        addBooleanProperty(GuiUtils.PROPERTY_SHOW_PROGRESS, () -> tile.getProgress() > 0);
        addIntProperty(GuiUtils.PROPERTY_PROGRESS, tile::getProgress);
        addIntProperty(GuiUtils.PROPERTY_MAX_PROGRESS, tile::getMax);

        GuiUtils.createPlayerSlots(player, this, 8, 76, 8, 134);
        GuiUtils.createContainerSlots(GuiUtils.SLOTS_CONTAINER_INVENTORY,this, tile.inventory, 3, 1, 0, 62, 43);
    }


    @Override
    public GuiContainer createGui() {
        return new GuiMill(this);
    }


}
