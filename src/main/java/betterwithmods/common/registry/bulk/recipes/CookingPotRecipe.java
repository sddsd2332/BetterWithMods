package betterwithmods.common.registry.bulk.recipes;

import betterwithmods.api.recipe.input.IRecipeInputs;
import betterwithmods.api.recipe.output.IRecipeOutputs;
import betterwithmods.api.tile.IHeatRecipe;

public class CookingPotRecipe<V extends CookingPotRecipe<V>> extends BulkRecipe<V> implements IHeatRecipe {

    private final int heat;
    private boolean ignoreHeat;

    public CookingPotRecipe(IRecipeInputs recipeInputs, IRecipeOutputs recipeOutput, int heat) {
        super(recipeInputs, recipeOutput);
        this.heat = heat;
    }

    @Override
    public int getHeat() {
        return heat;
    }

    @Override
    public boolean ignore() {
        return ignoreHeat;
    }

    public CookingPotRecipe setIgnoreHeat(boolean ignoreHeat) {
        this.ignoreHeat = ignoreHeat;
        return this;
    }

    @Override
    public CookingPotRecipe setPriority(int priority) {
        return (CookingPotRecipe) super.setPriority(priority);
    }
}
