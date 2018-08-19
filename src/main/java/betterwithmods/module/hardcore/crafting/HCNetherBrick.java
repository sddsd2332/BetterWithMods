package betterwithmods.module.hardcore.crafting;

import betterwithmods.common.BWMRecipes;
import betterwithmods.module.Feature;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class HCNetherBrick extends Feature {

    @Override
    public void onInit(FMLInitializationEvent event) {
        BWMRecipes.removeFurnaceRecipe(new ItemStack(Blocks.NETHERRACK));
    }

    @Override
    public String getDescription() {
        return "Remove the recipe for smelting netherrack into netherbrick";
    }
}
