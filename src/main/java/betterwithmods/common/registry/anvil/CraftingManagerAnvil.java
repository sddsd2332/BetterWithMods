package betterwithmods.common.registry.anvil;

import betterwithmods.common.registry.base.CraftingManagerBase;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class CraftingManagerAnvil extends CraftingManagerBase<IRecipe> {

    private IRecipe recipe;

    /**
     * Retrieves an ItemStack that has multiple recipes for it.
     */
    public ItemStack findMatchingResult(InventoryCrafting inventory, World world) {

        if (recipe != null && recipe.matches(inventory, world)) {
            return recipe.getCraftingResult(inventory);
        }

        for (IRecipe irecipe : recipes) {
            if (irecipe.matches(inventory, world)) {
                recipe = irecipe;
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

        for (IRecipe irecipe : recipes) {
            if (irecipe.matches(inventory, craftMatrix)) {
                recipe = irecipe;
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


    @Override
    public List<IRecipe> getDisplayRecipes() {
        return recipes;
    }

    @Override
    public IRecipe addRecipe(@Nonnull IRecipe recipe) {
        this.recipes.add(recipe);
        return recipe;
    }
}