package betterwithmods.module.tweaks;


import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.recipes.RecipeMatchers;
import betterwithmods.library.common.recipes.RecipeRemover;
import betterwithmods.module.internal.RecipeRegistry;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CheaperAxes extends Feature {

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Items.STONE_AXE.getRegistryName()));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Items.IRON_AXE.getRegistryName()));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Items.GOLDEN_AXE.getRegistryName()));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Items.DIAMOND_AXE.getRegistryName()));
    }

    @Override
    public String getDescription() {
        return "Change vanilla axe recipes to only require 2 material rather than 3.";
    }
}
