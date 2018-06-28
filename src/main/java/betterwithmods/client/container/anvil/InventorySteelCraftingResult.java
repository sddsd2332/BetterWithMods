package betterwithmods.client.container.anvil;

import betterwithmods.common.blocks.tile.TileSteelAnvil;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

import javax.annotation.Nonnull;

public class InventorySteelCraftingResult extends InventoryCraftResult {

    private final TileSteelAnvil craft;

    public InventorySteelCraftingResult(TileSteelAnvil te) {
        craft = te;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot == 0 ? craft.getResult() : null;
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int slot, int decrement) {
        //return craft.decrStackSize(slot, decrement);
        craft.getWorld().playSound(null,craft.getPos(),SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS,0.5f,1.0f);
        ItemStack stack = craft.getResult();
        if (stack != null) {
            craft.setResult(null);
            return stack;
        } else {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, @Nonnull ItemStack stack) {
        craft.setResult(stack);
    }
}
