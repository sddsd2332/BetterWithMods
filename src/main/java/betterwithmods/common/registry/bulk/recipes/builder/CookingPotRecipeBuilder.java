package betterwithmods.common.registry.bulk.recipes.builder;

import betterwithmods.common.registry.bulk.recipes.CookingPotRecipe;
import betterwithmods.common.registry.heat.BWMHeatRegistry;

public abstract class CookingPotRecipeBuilder<V extends CookingPotRecipe<V>> extends BulkRecipeBuilder<V> {

    protected int heat = 1;
    protected boolean ignoreHeat = false;

    public CookingPotRecipeBuilder<V> heat(int heat) {
        this.heat = heat;
        return this;
    }

    public CookingPotRecipeBuilder<V> stoked() {
        return this.heat(BWMHeatRegistry.STOKED_HEAT);
    }

    public CookingPotRecipeBuilder<V> unstoked() {
        return this.heat(BWMHeatRegistry.UNSTOKED_HEAT);
    }

    public CookingPotRecipeBuilder<V> ignoreHeat() {
        this.ignoreHeat = true;
        return this;
    }

    @Override
    public void reset() {
        super.reset();
        this.heat = 1;
        this.ignoreHeat = false;
    }
}
