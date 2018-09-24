package betterwithmods.module.hardcore.crafting;

import betterwithmods.BWMod;
import betterwithmods.common.BWMRecipes;
import betterwithmods.common.BWMRegistry;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.library.modularity.impl.Feature;
import betterwithmods.module.recipes.MetalReclaming;
import betterwithmods.module.tweaks.CheaperAxes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_axe"));
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_hoe"));
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_pickaxe"));
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_sword"));
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_shovel"));
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_helmet"));
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_chestplate"));
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_leggings"));
        BWMRecipes.removeRecipe(new ResourceLocation("minecraft:diamond_boots"));
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        if (BWMod.MODULE_LOADER.isFeatureEnabled(MetalReclaming.class) && MetalReclaming.reclaimCount > 0) {
            if (BWMod.MODULE_LOADER.isFeatureEnabled(CheaperAxes.class)) {
                BWMRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_AXE, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 2));

            } else {
                BWMRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_AXE, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 3));
            }
            BWMRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_HOE, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 2));
            BWMRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_PICKAXE, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 3));
            BWMRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_SHOVEL, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 1));
            BWMRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_SWORD, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 2));

            BWMRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_HELMET, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 5));
            BWMRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 8));
            BWMRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 7));
            BWMRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.DIAMOND_BOOTS, 1, OreDictionary.WILDCARD_VALUE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.DIAMOND_INGOT, 4));

        }
    }


}
