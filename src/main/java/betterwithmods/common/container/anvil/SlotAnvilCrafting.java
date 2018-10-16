package betterwithmods.common.container.anvil;

import betterwithmods.module.internal.RecipeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class SlotAnvilCrafting extends SlotCrafting {

    private final InventoryAnvilCrafting craftingMatrix;
    private final EntityPlayer player;

    public SlotAnvilCrafting(EntityPlayer player, InventoryAnvilCrafting craftingMatrix, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(player, craftingMatrix, inventoryIn, slotIndex, xPosition, yPosition);
        this.craftingMatrix = craftingMatrix;
        this.player = player;
    }

    @Override
    @Nonnull
    public ItemStack onTake(EntityPlayer player, @Nonnull ItemStack stack) {
        this.onCrafting(stack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(player);
        NonNullList<ItemStack> nonnulllist = RecipeRegistry.ANVIL.getRemainingItems(craftingMatrix, player.getEntityWorld());
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = this.craftingMatrix.getStackInSlot(i);
            ItemStack itemstack1 = nonnulllist.get(i);

            if (!itemstack.isEmpty()) {
                this.craftingMatrix.decrStackSize(i, 1);
                itemstack = this.craftingMatrix.getStackInSlot(i);
            }

            if (!itemstack1.isEmpty()) {
                if (itemstack.isEmpty()) {
                    this.craftingMatrix.setInventorySlotContents(i, itemstack1);
                } else if (ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1)) {
                    itemstack1.grow(itemstack.getCount());
                    this.craftingMatrix.setInventorySlotContents(i, itemstack1);
                } else if (!this.player.inventory.addItemStackToInventory(itemstack1)) {
                    this.player.dropItem(itemstack1, false);
                }
            }
        }

        return stack;
    }


}
