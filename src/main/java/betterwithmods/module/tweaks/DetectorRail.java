package betterwithmods.module.tweaks;

import betterwithmods.BetterWithMods;
import betterwithmods.common.BWMRecipes;
import betterwithmods.common.blocks.BlockRailDetectorBase;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.library.modularity.impl.Feature;
import betterwithmods.library.recipes.RecipeMatchers;
import betterwithmods.library.recipes.RecipeRemover;
import betterwithmods.module.internal.BlockRegistry;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.module.recipes.MetalReclaming;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.Objects;


public class DetectorRail extends Feature {

    public static final Block DETECTOR_RAIL_STONE = new BlockRailDetectorBase(cart -> !(cart instanceof EntityMinecartEmpty) || BlockRailDetectorBase.isRider(cart, Objects::nonNull)).setRegistryName("detector_rail_stone");
    public static final Block DETECTOR_RAIL_STEEL = new BlockRailDetectorBase(cart -> BlockRailDetectorBase.isRider(cart, rider -> rider instanceof EntityPlayer)).setRegistryName("detector_rail_steel");

    @Override
    public String getDescription() {
        return "Change what detector rails detect; Wooden:all minecarts; Stone: carts containing something, SFS: carts with players.";
    }

    @Override
    public void onPreInitClient(FMLPreInitializationEvent event) {
        //TODO
//        config().overrideBlock("rail_detector");
//        config().overrideBlock("rail_detector_powered");
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        BlockRegistry.registerBlock(DETECTOR_RAIL_STEEL);
        BlockRegistry.registerBlock(DETECTOR_RAIL_STONE);

        Blocks.DETECTOR_RAIL.setTranslationKey("detector_rail_wood");

        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_NAME, Blocks.DETECTOR_RAIL.getRegistryName()));
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        if (BetterWithMods.MODULE_LOADER.isFeatureEnabled(MetalReclaming.class)) {
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(DETECTOR_RAIL_STONE, 6), new ItemStack(Items.IRON_INGOT, 6));
            RecipeRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(DETECTOR_RAIL_STEEL, 6), Lists.newArrayList(new ItemStack(Items.IRON_INGOT, 6), ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT, 2)));
        }
    }
}
