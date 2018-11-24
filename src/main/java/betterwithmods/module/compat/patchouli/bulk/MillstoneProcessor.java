package betterwithmods.module.compat.patchouli.bulk;

import betterwithmods.common.registry.bulk.recipes.MillRecipe;
import betterwithmods.module.internal.RecipeRegistry;

public class MillstoneProcessor extends BulkProcessor<MillRecipe> {
    public MillstoneProcessor() {
        super(RecipeRegistry.MILLSTONE, 3, 3);
    }
}
