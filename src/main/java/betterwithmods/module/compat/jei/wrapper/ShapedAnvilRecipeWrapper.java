package betterwithmods.module.compat.jei.wrapper;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapelessRecipeWrapper;
import net.minecraftforge.common.crafting.IShapedRecipe;

public class ShapedAnvilRecipeWrapper extends ShapelessRecipeWrapper<IShapedRecipe> implements IShapedCraftingRecipeWrapper {
    public ShapedAnvilRecipeWrapper(IJeiHelpers helpers, IShapedRecipe recipe) {
        super(helpers, recipe);
    }

    @Override
    public int getWidth() {
        return recipe.getRecipeWidth();
    }

    @Override
    public int getHeight() {
        return recipe.getRecipeHeight();
    }
}
