package betterwithmods.module.recipes;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockRawPastry;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.bulk.recipes.builder.MillRecipeBuilder;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.common.recipes.RecipeMatchers;
import betterwithmods.library.common.recipes.RecipeRemover;
import betterwithmods.library.utils.colors.ColorUtils;
import betterwithmods.library.utils.ingredient.StackIngredient;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import betterwithmods.module.internal.RecipeRegistry;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreIngredient;

/**
 * Created by primetoxinz on 5/16/17.
 */
public class MillRecipes extends Feature {
    private boolean grindingOnly;


    @Override
    public String getDescription() {
        return null;
    }

    @Override
    protected boolean canEnable() {
        return true;
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        grindingOnly = loadProperty("Grinding Only", true).setComment("Remove normal recipes for certain grindable items").get();
    }

    @Override
    public void registerRecipes() {
        if (grindingOnly) {
            RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.OUTPUT, new ItemStack(Items.SUGAR)));
            RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.OUTPUT, new ItemStack(Items.BLAZE_POWDER)));
            RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.OUTPUT, new ItemStack(Items.BLAZE_POWDER)));
            RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.INPUT, Lists.newArrayList(StackIngredient.fromStacks(new ItemStack(Items.BEETROOT)))));

            for (BlockStateIngredient flower : ColorUtils.FLOWER_TO_DYES.keySet()) {
                RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.INPUT, Lists.newArrayList(flower)));
            }
        }
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        MillRecipeBuilder builder = new MillRecipeBuilder();
        RecipeRegistry.MILLSTONE.registerAll(
                builder.sound(SoundEvents.ENTITY_GHAST_SCREAM).inputs(new OreIngredient("netherrack")).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.GROUND_NETHERRACK)).build(),
                builder.sound(SoundEvents.ENTITY_BLAZE_DEATH).inputs(new ItemStack(Items.BLAZE_ROD)).outputs(new ItemStack(Items.BLAZE_POWDER, 3)).build(),
                builder.sound(SoundEvents.ENTITY_WOLF_WHINE).inputs(new ItemStack(BWMBlocks.WOLF)).outputs(new ItemStack(Items.STRING, 10), ColorUtils.getDye(EnumDyeColor.RED, 3)).build(),
                builder.inputs(new OreIngredient("cropHemp")).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.HEMP_FIBERS, 3)).build(),
                builder.inputs(new ItemStack(BWMBlocks.WOLF)).outputs(new ItemStack(Items.STRING, 10), ColorUtils.getDye(EnumDyeColor.RED, 3)).build(),
                builder.inputs(new ItemStack(Items.REEDS)).outputs(new ItemStack(Items.SUGAR, 2)).build(),
                builder.inputs(new ItemStack(Items.COAL, 1)).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.COAL_DUST)).build(),
                builder.inputs(new ItemStack(Items.COAL, 1, 1)).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.CHARCOAL_DUST)).build(),
                builder.inputs(new ItemStack(Items.BONE)).outputs(ColorUtils.getDye(EnumDyeColor.WHITE, 6)).build(),
                builder.inputs(new ItemStack(Items.SKULL, 1, 0)).outputs(ColorUtils.getDye(EnumDyeColor.WHITE, 10)).build(),
                builder.inputs(new ItemStack(Blocks.BONE_BLOCK)).outputs(ColorUtils.getDye(EnumDyeColor.WHITE, 9)).build(),
                builder.inputs(new ItemStack(Items.BEETROOT)).outputs(ColorUtils.getDye(EnumDyeColor.RED, 2)).build(),
                builder.inputs(new ItemStack(Items.LEATHER)).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER)).build(),
                builder.inputs(new ItemStack(Items.RABBIT_HIDE)).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT)).build(),
                builder.inputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.LEATHER_CUT)).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT)).build(),
                builder.inputs(new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage())).outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.COCOA_POWDER)).build(),
                builder.inputs(new OreIngredient("cropWheat")).outputs(BlockRawPastry.getStack(BlockRawPastry.Type.BREAD)).build(),
                builder.inputs(new OreIngredient("cropBarley")).outputs(BlockRawPastry.getStack(BlockRawPastry.Type.BREAD)).build(),
                builder.inputs(new OreIngredient("cropOats")).outputs(BlockRawPastry.getStack(BlockRawPastry.Type.BREAD)).build(),
                builder.inputs(new OreIngredient("cropRye")).outputs(BlockRawPastry.getStack(BlockRawPastry.Type.BREAD)).build(),
                builder.inputs(new OreIngredient("cropRice")).outputs(BlockRawPastry.getStack(BlockRawPastry.Type.BREAD)).build()
        );

        for (BlockStateIngredient flower : ColorUtils.FLOWER_TO_DYES.keySet()) {
            RecipeRegistry.MILLSTONE.register(builder.inputs(flower).outputs(ColorUtils.FLOWER_TO_DYES.get(flower).getStack()).build());
        }
    }

}


