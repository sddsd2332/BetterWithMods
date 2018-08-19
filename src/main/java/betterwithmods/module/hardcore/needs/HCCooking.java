package betterwithmods.module.hardcore.needs;

import betterwithmods.common.BWMRecipes;
import betterwithmods.module.Feature;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class HCCooking extends Feature {

    @Override
    public String getDescription() {
        return "Changes the recipes for baked goods to require the Kiln and changes soups to require the Cauldron.";
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        BWMRecipes.removeRecipe(Items.MUSHROOM_STEW.getRegistryName());
        BWMRecipes.removeRecipe(Items.CAKE.getRegistryName());
        BWMRecipes.removeRecipe(Items.COOKIE.getRegistryName());
        BWMRecipes.removeRecipe(Items.PUMPKIN_PIE.getRegistryName());
        BWMRecipes.removeRecipe(Items.RABBIT_STEW.getRegistryName());
        BWMRecipes.removeRecipe(Items.BEETROOT_SOUP.getRegistryName());
        BWMRecipes.removeRecipe(Items.BREAD.getRegistryName());
    }


}
