package betterwithmods.module.compat.jei.wrapper;

import betterwithmods.common.registry.block.recipe.BlockRecipe;
import betterwithmods.module.compat.jei.JEILib;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

/**
 * Purpose:
 *
 * @author primetoxinz
 * @version 11/11/16
 */
public class BlockRecipeWrapper<T extends BlockRecipe> implements IRecipeWrapper {
    public final T recipe;
    protected final IJeiHelpers helpers;
    private final int boxes;

    public BlockRecipeWrapper(IJeiHelpers helpers, T recipe, int boxes) {
        this.helpers = helpers;
        this.recipe = recipe;
        this.boxes = boxes;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, helpers.getStackHelper().toItemStackList(recipe.getInput()));
        ingredients.setOutputLists(JEILib.IOUTPUT, recipe.getRecipeOutput().getExpandedOutputs(boxes));
    }


    public T getRecipe() {
        return recipe;
    }
}
