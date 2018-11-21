package betterwithmods.common.registry.bulk.recipes;

import betterwithmods.api.recipe.input.IRecipeInputs;
import betterwithmods.api.recipe.output.IRecipeOutputs;

public class CrucibleRecipe extends CookingPotRecipe<CrucibleRecipe> {

    public CrucibleRecipe(IRecipeInputs recipeInputs, IRecipeOutputs recipeOutput, int heat) {
        super(recipeInputs, recipeOutput, heat);
    }
}