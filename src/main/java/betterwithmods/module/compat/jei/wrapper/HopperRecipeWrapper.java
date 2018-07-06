package betterwithmods.module.compat.jei.wrapper;

import betterwithmods.api.recipe.output.IOutput;
import betterwithmods.common.registry.hopper.recipes.HopperRecipe;
import com.google.common.collect.Lists;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Purpose:
 *
 * @author primetoxinz
 * @version 11/20/16
 */
public class HopperRecipeWrapper implements IRecipeWrapper {

    public final HopperRecipe recipe;
    private final IJeiHelpers helpers;

    public HopperRecipeWrapper(IJeiHelpers helpers, HopperRecipe recipe) {
        this.recipe = recipe;
        this.helpers = helpers;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(ItemStack.class, Lists.newArrayList(helpers.getStackHelper().toItemStackList(recipe.getInputs()), recipe.getFilters(), recipe.getInputContainer()));
        List<List<IOutput>> outputs = recipe.getRecipeOutputWorld().getExpandedOutputs(2);
        outputs.addAll(recipe.getRecipeOutputInsert().getExpandedOutputs(2));
        ingredients.setOutputLists(IOutput.class, outputs);
        ingredients.setOutputs(ItemStack.class, recipe.getOutputContainer());
    }

}
