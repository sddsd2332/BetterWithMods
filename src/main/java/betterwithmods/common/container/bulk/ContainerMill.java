package betterwithmods.common.container.bulk;

import betterwithmods.client.gui.bulk.GuiMill;
import betterwithmods.common.container.ProgressSource;
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

        GuiUtils.addPlayerInventory(player, this, 8, 76, 8, 134);
        GuiUtils.createSlots(this, tile.inventory, 3, 1, 0, 62, 43);
    }


    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if (index < 3) {
                if (!mergeItemStack(stack1, 3, this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!mergeItemStack(stack1, 0, 3, false))
                return ItemStack.EMPTY;
            if (stack1.getCount() == 0)
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();
        }
        return stack;
    }


    @Override
    public GuiContainer createGui() {
        return new GuiMill(this);
    }


}
