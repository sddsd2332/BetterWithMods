package betterwithmods.common.container.anvil;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class InventoryAnvilCrafting extends InventoryCrafting implements IItemHandlerModifiable {

    public Container container;
    private IItemHandlerModifiable inventory;

    InventoryAnvilCrafting(Container container, IItemHandlerModifiable inventory) {
        super(container, 4, 4);
        this.container = container;
        this.inventory = inventory;

    }

    @Override
    public int getSlots() {
        return inventory.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return inventory.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return inventory.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return inventory.getSlotLimit(slot);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        inventory.setStackInSlot(slot, stack);
    }

    /**
     * IInventory
     **/

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = inventory.extractItem(index, count, false);
        if (!stack.isEmpty()) {
            container.onCraftMatrixChanged(this);
        }
        return stack;
    }


    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        inventory.setStackInSlot(index, stack);
        container.onCraftMatrixChanged(this);
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory.getStackInSlot(index);
    }
}
