package betterwithmods.common.registry.bulk.recipes.builder;

import betterwithmods.common.registry.bulk.recipes.CauldronRecipe;

public class CauldronRecipeBuilder extends CookingPotRecipeBuilder<CauldronRecipe> {
    @Override
    protected CauldronRecipe create() {
        return new CauldronRecipe(inputs, outputs, heat);
    }
}
