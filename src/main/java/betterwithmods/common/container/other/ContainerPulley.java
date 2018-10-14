package betterwithmods.common.container.other;

import betterwithmods.common.tile.TilePulley;
import betterwithmods.library.common.container.ContainerTile;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerPulley extends ContainerTile<TilePulley> {
    private static final int ROPE_SLOTS_END = 4;

    public ContainerPulley(TilePulley tile, EntityPlayer player) {
        super(tile, player);
    }

//    public ContainerPulley(EntityPlayer player, TilePulley tile) {
//        super(tile);
//        this.tile = tile;
//
//        for (int i = 0; i < ROPE_SLOTS_END; i++) {
//            addSlotToContainer(new SlotItemHandler(tile.inventory, i, 53 + i * 18, 52) {
//                @Override
//                public boolean isItemValid(@Nonnull ItemStack stack) {
//                    return super.isItemValid(stack) && stack.getItem() == Item.getItemFromBlock(BWMBlocks.ROPE);
//                }
//            });
//        }
//
//
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 9; j++) {
//                addSlotToContainer(new SlotItemHandler(player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), j + i * 9 + 9, 8 + j * 18, 93 + i * 18));
//            }
//        }
//
//        for (int i = 0; i < 9; i++) {
//            addSlotToContainer(new SlotItemHandler(player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null), i, 8 + i * 18, 151));
//        }
//    }

    @Override
    public GuiContainer createGui() {
        return null;
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack clickedStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack processedStack = slot.getStack();
            clickedStack = processedStack.copy();

            if (index < ROPE_SLOTS_END) {
                if (!mergeItemStack(processedStack, ROPE_SLOTS_END, this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!mergeItemStack(processedStack, 0, ROPE_SLOTS_END, false)) {
                return ItemStack.EMPTY;
            }

            if (processedStack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return clickedStack;
    }
}
