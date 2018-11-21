package betterwithmods.common.registry.block.recipe.builder;

import betterwithmods.common.registry.block.recipe.SawRecipe;
import net.minecraft.item.ItemStack;

public class SawRecipeBuilder extends BlockRecipeBuilder<SawRecipe> {

    public SawRecipeBuilder selfdrop(ItemStack stack) {
        this.input(stack);
        this.outputs(stack);
        return this;
    }


    @Override
    protected SawRecipe create() {
        return new SawRecipe(input, outputs);
    }

}
