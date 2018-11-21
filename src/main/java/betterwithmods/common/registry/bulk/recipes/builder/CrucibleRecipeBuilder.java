package betterwithmods.common.registry.bulk.recipes.builder;

import betterwithmods.common.registry.bulk.recipes.CrucibleRecipe;

public class CrucibleRecipeBuilder extends CookingPotRecipeBuilder<CrucibleRecipe> {
    @Override
    protected CrucibleRecipe create() {
        return new CrucibleRecipe(inputs, outputs, heat);
    }
}
