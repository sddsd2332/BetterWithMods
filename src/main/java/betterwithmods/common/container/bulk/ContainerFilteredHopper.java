package betterwithmods.common.container.bulk;

import betterwithmods.library.common.container.ContainerProgress;
import betterwithmods.common.registry.hopper.filters.HopperFilter;
import betterwithmods.common.tile.TileFilteredHopper;
import betterwithmods.library.utils.CapabilityUtils;
import betterwithmods.module.internal.RecipeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerFilteredHopper extends ContainerProgress {
    private final TileFilteredHopper tile;

    public ContainerFilteredHopper(EntityPlayer player, TileFilteredHopper tile) {
        super(tile);
        this.tile = tile;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new SlotItemHandler(tile.inventory, j + i * 9, 8 + j * 18, 60 + i * 18));
            }
        }

        addSlotToContainer(new SlotItemHandler(tile.filter, 0, 80, 37));

        IItemHandler playerInv = CapabilityUtils.getEntityInventory(player);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new SlotItemHandler(playerInv, j + i * 9 + 9, 8 + j * 18, 111 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new SlotItemHandler(playerInv, i, 8 + i * 18, 169));
        }
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack clickedStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack processedStack = slot.getStack();
            clickedStack = processedStack.copy();

            if (index < 19) {
                if (!mergeItemStack(processedStack, 19, this.inventorySlots.size(), true))
                    return ItemStack.EMPTY;
            } else if (RecipeRegistry.HOPPER_FILTERS.isFilter(processedStack)) {
                if (!mergeItemStack(processedStack, 18, 19, false))
                    return ItemStack.EMPTY;
            } else if (!mergeItemStack(processedStack, 0, 18, false)) {
                return ItemStack.EMPTY;
            }

            if (processedStack.getCount() == 0)
                slot.putStack(ItemStack.EMPTY);
            else
                slot.onSlotChanged();
        }
        return clickedStack;
    }

    private boolean isItemFilter(ItemStack stack) {
        return RecipeRegistry.HOPPER_FILTERS.getFilter(stack) != HopperFilter.NONE;
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return tile.isUseableByPlayer(playerIn);
    }

}
