package betterwithmods.module.recipes;

import betterwithmods.common.BWMOreDictionary;
import betterwithmods.common.BWMRegistry;
import betterwithmods.library.modularity.impl.Feature;
import betterwithmods.library.utils.ingredient.StackIngredient;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

/**
 * Created by primetoxinz on 4/21/17.
 */
public class NuggetCompression extends Feature {

    @Override
    public String getDescription() {
        return "Adds recipes to the Crucible to compact 9 Nuggets into it's corresponding Ingot.";
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        for (BWMOreDictionary.Ore ingot : BWMOreDictionary.ingotNames) {
            String suffix = ingot.getSuffix();
            if (!ingot.getOres().isEmpty() && suffix != null) {
                StackIngredient nugget = StackIngredient.fromOre(9, "nugget" + suffix);
                BWMRegistry.CRUCIBLE.addStokedRecipe(nugget, ingot.getOres().get(0));
            }
        }
    }
}
