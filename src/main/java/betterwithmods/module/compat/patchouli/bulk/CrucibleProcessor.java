package betterwithmods.module.compat.patchouli.bulk;

import betterwithmods.common.registry.bulk.recipes.CrucibleRecipe;
import betterwithmods.module.internal.RecipeRegistry;

public class CrucibleProcessor extends BulkProcessor<CrucibleRecipe> {
    public CrucibleProcessor() {
        super(RecipeRegistry.CRUCIBLE, 6, 3);
    }

    @Override
    public String process(String key) {
        if (key.equals("heat")) {
            switch (recipe.getHeat()) {
                default:
                    return "betterwithmods:textures/gui/book/cooking_pot_1.png";
                case 2:
                    return "betterwithmods:textures/gui/book/cooking_pot_2.png";
            }
        }
        return super.process(key);
    }


}
