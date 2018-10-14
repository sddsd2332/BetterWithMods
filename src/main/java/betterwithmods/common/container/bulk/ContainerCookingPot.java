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
        GuiUtils.addPlayerInventory(player, this, 8, 111, 8, 169);
        GuiUtils.createSlots(this, tile.inventory, 27, 3, 0, 8, 43);
    }


    //TODO
//    @Nonnull
//    @Override
//    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
//        ItemStack stack = ItemStack.EMPTY;
//        Slot slot = this.inventorySlots.get(slotIndex);
//
//        if (slot != null && slot.getHasStack()) {
//            ItemStack stack1 = slot.getStack();
//            stack = stack1.copy();
//
//            if (slotIndex < 27) {
//                if (!mergeItemStack(stack1, 27, this.inventorySlots.size(), true))
//                    return ItemStack.EMPTY;
//            } else if (!mergeItemStack(stack1, 0, 27, false))
//                return ItemStack.EMPTY;
//
//            if (stack1.getCount() < 1)
//                slot.putStack(ItemStack.EMPTY);
//            else
//                slot.onSlotChanged();
//        }
//        return stack;
//    }

}
