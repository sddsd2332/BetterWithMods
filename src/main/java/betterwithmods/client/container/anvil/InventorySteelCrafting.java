package betterwithmods.client.container.anvil;

import betterwithmods.common.tile.TileSteelAnvil;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class InventorySteelCrafting extends InventoryCrafting {

    public final TileSteelAnvil craft;
    public final Container container;
    private final IItemHandler handler;

    public InventorySteelCrafting(Container container, TileSteelAnvil te) {
        super(container, 4, 4);
        craft = te;
        handler = te.inventory;
        this.container = container;
    }


    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot >= this.getSizeInventory() ? ItemStack.EMPTY : handler.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack getStackInRowAndColumn(int row, int column) {
        if (row >= 0 && row < 4) {
            int x = row + column * 4;
            return this.getStackInSlot(x);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int slot, int decrement) {
        ItemStack stack = handler.getStackInSlot(slot);
        this.container.onCraftMatrixChanged(this);
        ItemStack itemstack;
        if (stack.getCount() <= decrement) {
            itemstack = stack.copy();
            craft.setInventorySlotContents(slot, ItemStack.EMPTY);
            this.container.onCraftMatrixChanged(this);
            return itemstack;
        } else {
            itemstack = stack.splitStack(decrement);
            if (stack.getCount() == 0) {
                craft.setInventorySlotContents(slot, ItemStack.EMPTY);
            }
            this.container.onCraftMatrixChanged(this);
            return itemstack;
        }
    }

//    public void craft() {
//        for (int i = 0; i < handler.getSlots(); i++) {
//            if (!handler.getStackInSlot(i).isEmpty()) {
//                handler.extractItem(i, 1, false);
//            }
//        }
//        this.container.onCraftMatrixChanged(this);
//    }

    @Override
    public void setInventorySlotContents(int slot, @Nonnull ItemStack itemstack) {
        craft.setInventorySlotContents(slot, itemstack);
        this.container.onCraftMatrixChanged(this);
    }

}