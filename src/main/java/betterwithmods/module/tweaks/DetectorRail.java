package betterwithmods.module.tweaks;

import betterwithmods.BetterWithMods;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.recipes.RecipeMatchers;
import betterwithmods.library.common.recipes.RecipeRemover;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.module.recipes.MetalReclaming;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


public class DetectorRail extends Feature {

    @Override
    public String getDescription() {
        return "Change what detector rails detect; Wooden:all minecarts; Stone: carts containing something, SFS: carts with players.";
    }

    @Override
    public void onPreInitClient(FMLPreInitializationEvent event) {
        config().overrideBlockResource("rail_detector");
        config().overrideBlockResource("rail_detector_powered");
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        Blocks.DETECTOR_RAIL.setTranslationKey(ModLib.MODID + ":detector_rail_wood");
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Blocks.DETECTOR_RAIL.getRegistryName()));
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        if (BetterWithMods.MODULE_LOADER.isFeatureEnabled(MetalReclaming.class)) {
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(BWMBlocks.DETECTOR_RAIL_STONE, 6), new ItemStack(Items.IRON_INGOT, 6));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(BWMBlocks.DETECTOR_RAIL_STEEL, 6), Lists.newArrayList(new ItemStack(Items.IRON_INGOT, 6), ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 2)));
        }
    }
}
