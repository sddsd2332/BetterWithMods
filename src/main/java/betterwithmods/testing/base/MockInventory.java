package betterwithmods.testing.base;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class MockInventory extends ItemStackHandler {
    public MockInventory(int size) {
        super(size);
    }

    public static MockInventory createInventory(int startIndex, ItemStack... array) {
        MockInventory inventory = new MockInventory(array.length);

        for (int i = startIndex; i < Math.min(array.length, inventory.getSlots()); i++) {
            int j = i - startIndex;
            inventory.setStackInSlot(i, array[j]);
        }
        return inventory;
    }
}
