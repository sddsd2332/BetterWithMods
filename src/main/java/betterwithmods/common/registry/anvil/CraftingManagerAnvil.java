package betterwithmods.common.registry.anvil;

import betterwithmods.common.registry.base.CraftingManagerBase;
import betterwithmods.lib.ModLib;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class CraftingManagerAnvil extends CraftingManagerBase<AnvilRecipe> {

    private IRecipe recipe;

    public CraftingManagerAnvil() {
        super(new ResourceLocation(ModLib.MODID, "anvil"), AnvilRecipe.class);
    }

    /**
     * Retrieves an ItemStack that has multiple recipes for it.
     */
    public ItemStack findMatchingResult(InventoryCrafting inventory, World world) {

        if (recipe != null && recipe.matches(inventory, world)) {
            return recipe.getCraftingResult(inventory);
        }

        for (AnvilRecipe irecipe : getValuesCollection()) {
            if (irecipe.matches(inventory, world)) {
                recipe = irecipe.getRecipe();
                return irecipe.getCraftingResult(inventory);
            }
        }

        for (IRecipe irecipe : CraftingManager.REGISTRY) {
            if (irecipe.matches(inventory, world)) {
                recipe = irecipe;
                return irecipe.getCraftingResult(inventory);
            }
        }

        return ItemStack.EMPTY;
    }


    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inventory, World craftMatrix) {

        if (recipe != null && recipe.matches(inventory, craftMatrix)) {
            return recipe.getRemainingItems(inventory);
        }

        for (AnvilRecipe irecipe : getValuesCollection()) {
            if (irecipe.matches(inventory, craftMatrix)) {
                recipe = irecipe.getRecipe();
                return irecipe.getRemainingItems(inventory);
            }
        }

        for (IRecipe irecipe : CraftingManager.REGISTRY) {
            if (irecipe.matches(inventory, craftMatrix)) {
                recipe = irecipe;
                return irecipe.getRemainingItems(inventory);
            }
        }

        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inventory.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i) {
            nonnulllist.set(i, inventory.getStackInSlot(i));
        }

        return nonnulllist;
    }
}