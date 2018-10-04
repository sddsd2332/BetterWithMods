package betterwithmods.module.hardcore.needs;

import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.recipes.RecipeMatchers;
import betterwithmods.library.common.recipes.RecipeRemover;
import betterwithmods.module.internal.RecipeRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class HCCooking extends Feature {

    @Override
    public String getDescription() {
        return "Changes the recipes for baked goods to require the Kiln and changes soups to require the Cauldron.";
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Blocks.TNT.getRegistryName()));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Items.MUSHROOM_STEW.getRegistryName()));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Items.CAKE.getRegistryName()));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Items.COOKIE.getRegistryName()));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Items.PUMPKIN_PIE.getRegistryName()));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Items.RABBIT_STEW.getRegistryName()));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Items.BEETROOT_SOUP.getRegistryName()));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Items.BREAD.getRegistryName()));
    }


}
