package betterwithmods.module.hardcore.crafting;

import betterwithmods.BetterWithMods;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.recipes.RecipeMatchers;
import betterwithmods.library.common.recipes.RecipeRemover;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.module.recipes.MetalReclaming;
import betterwithmods.module.tweaks.CheaperAxes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class HCDiamond extends Feature {
    @Override
    public String getDescription() {
        return "Makes it so diamonds have to be made into an ingot alloy to be used in certain recipes";
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:diamond_axe"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:diamond_hoe"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:diamond_pickaxe"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:diamond_sword"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:diamond_shovel"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:diamond_helmet"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:diamond_chestplate"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:diamond_leggings"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:diamond_boots"));
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        if (BetterWithMods.MODULE_LOADER.isFeatureEnabled(MetalReclaming.class) && MetalReclaming.reclaimCount > 0) {
            if (BetterWithMods.MODULE_LOADER.isFeatureEnabled(CheaperAxes.class)) {
                RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_AXE, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 2));

            } else {
                RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_AXE, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 3));
            }
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_HOE, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 2));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_PICKAXE, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 3));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_SHOVEL, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 1));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_SWORD, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 2));

            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_HELMET, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 5));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 8));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 7));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_BOOTS, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 4));

        }
    }


}
