package betterwithmods.module.compat.patchouli.block;

import betterwithmods.common.registry.block.recipe.SawRecipe;
import betterwithmods.module.internal.RecipeRegistry;

public class SawProcessor extends BlockProcessor<SawRecipe> {

    public SawProcessor() {
        super(RecipeRegistry.WOOD_SAW, 3);
    }
}
