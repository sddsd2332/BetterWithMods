package betterwithmods.common.registry.block.recipe.builder;

import betterwithmods.common.registry.block.recipe.KilnRecipe;
import betterwithmods.common.registry.heat.BWMHeatRegistry;

public class KilnRecipeBuilder extends BlockRecipeBuilder<KilnRecipe> {

    private int heat = 1;
    protected boolean ignoreHeat = false, heatScale = true;
    private int cookTime;

    public KilnRecipeBuilder heat(int heat) {
        this.heat = heat;
        return this;
    }

    public KilnRecipeBuilder stoked() {
        return this.heat(BWMHeatRegistry.STOKED_HEAT);
    }

    public KilnRecipeBuilder unstoked() {
        return this.heat(BWMHeatRegistry.UNSTOKED_HEAT);
    }

    public KilnRecipeBuilder ignoreHeat() {
        this.ignoreHeat = true;
        return this;
    }

    public KilnRecipeBuilder noHeatScale() {
        this.heatScale = false;
        return this;
    }

    @Override
    protected KilnRecipe create() {
        KilnRecipe recipe = new KilnRecipe(input, outputs, heat);
        recipe.cookTime(cookTime * (heatScale ? heat : 1));
        recipe.ignoreHeat(ignoreHeat);
        return recipe;
    }

    @Override
    public void reset() {
        super.reset();
        heat = 1;
        ignoreHeat = false;
        heatScale = true;
        cookTime = 500;
    }
}
