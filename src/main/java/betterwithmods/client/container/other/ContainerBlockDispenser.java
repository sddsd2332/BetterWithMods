package betterwithmods.client.container.other;

import betterwithmods.common.blocks.tile.TileBlockDispenser;
import betterwithmods.util.CapabilityUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerBlockDispenser extends Container {
    private final TileBlockDispenser tile;
    private int nextSlot;

    public ContainerBlockDispenser(EntityPlayer player, TileBlockDispenser tile) {
        this.tile = tile;

        IItemHandler playerInv = CapabilityUtils.getEntityInventory(player);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                addSlotToContainer(new SlotItemHandler(tile.inventory, j + i * 4, 53 + j * 18, 17 + i * 18));
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new SlotItemHandler(playerInv, j + i * 9 + 9, 8 + j * 18, 102 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new SlotItemHandler(playerInv, i, 8 + i * 18, 160));
        }
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return tile.isUseableByPlayer(player);
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();

            if (slotIndex < 16) {
                if (!mergeItemStack(stack1, 16, this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!mergeItemStack(stack1, 0, 16, false))
                return ItemStack.EMPTY;

            if (stack1.getCount() == 0)
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();
        }
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack slotClick(int x, int dragType, ClickType type, EntityPlayer player) {
        if (x < 16)
            this.tile.nextIndex = 0;

        return super.slotClick(x, dragType, type, player);
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendWindowProperty(this, 0, this.tile.nextIndex);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener craft : this.listeners) {
            if (this.nextSlot != this.tile.nextIndex)
                craft.sendWindowProperty(this, 0, this.tile.nextIndex);
        }

        this.nextSlot = this.tile.nextIndex;
    }

    @Override
    public void updateProgressBar(int index, int value) {
        if (index == 0) {
            this.tile.nextIndex = value;
        }
    }

}
