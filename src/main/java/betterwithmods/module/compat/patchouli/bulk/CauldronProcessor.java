package betterwithmods.module.compat.patchouli.bulk;

import betterwithmods.common.registry.bulk.recipes.CauldronRecipe;
import betterwithmods.module.internal.RecipeRegistry;

public class CauldronProcessor extends BulkProcessor<CauldronRecipe> {
    public CauldronProcessor() {
        super(RecipeRegistry.CAULDRON, 9, 3);
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
