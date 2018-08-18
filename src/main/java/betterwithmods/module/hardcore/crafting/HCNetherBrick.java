package betterwithmods.module.hardcore.crafting;

import betterwithmods.common.BWMRecipes;
import betterwithmods.module.Feature;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class HCNetherBrick extends Feature {

    @Override
    public void onInit(FMlInitializationEvent event) {
        BWMRecipes.removeFurnaceRecipe(new ItemStack(Blocks.NETHERRACK));
    }

    @Override
    public String getDescription() {
        return "Remove the recipe for smelting netherrack into netherbrick";
    }
}
