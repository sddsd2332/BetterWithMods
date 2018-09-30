package betterwithmods.module.recipes;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMRecipes;
import betterwithmods.common.blocks.BlockRawPastry;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.library.recipes.RecipeMatcher;
import betterwithmods.library.recipes.RecipeMatchers;
import betterwithmods.library.recipes.RecipeRemover;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import betterwithmods.library.modularity.impl.Feature;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.module.internal.SoundRegistry;
import betterwithmods.library.utils.colors.ColorUtils;
import betterwithmods.library.utils.ingredient.StackIngredient;
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
        RecipeRegistry.MILLSTONE.addMillRecipe(new OreIngredient("netherrack"), Lists.newArrayList(ItemMaterial.getStack(ItemMaterial.EnumMaterial.GROUND_NETHERRACK)), SoundRegistry.BLOCK_GRIND_SCREAM);
        RecipeRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.BLAZE_ROD), Lists.newArrayList(new ItemStack(Items.BLAZE_POWDER, 3)), SoundEvents.ENTITY_BLAZE_DEATH);

        RecipeRegistry.MILLSTONE.addMillRecipe(new ItemStack(BWMBlocks.WOLF), Lists.newArrayList(new ItemStack(Items.STRING, 10), ColorUtils.getDye(EnumDyeColor.RED, 3)), SoundEvents.ENTITY_WOLF_WHINE);
        RecipeRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.REEDS), Lists.newArrayList(new ItemStack(Items.SUGAR, 2)));

        RecipeRegistry.MILLSTONE.addMillRecipe(new OreIngredient("cropHemp"), ItemMaterial.getStack(ItemMaterial.EnumMaterial.HEMP_FIBERS, 3));
        RecipeRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.COAL, 1), ItemMaterial.getStack(ItemMaterial.EnumMaterial.COAL_DUST));
        RecipeRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.COAL, 1, 1), ItemMaterial.getStack(ItemMaterial.EnumMaterial.CHARCOAL_DUST));
        RecipeRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.BONE), ColorUtils.getDye(EnumDyeColor.WHITE, 6));
        RecipeRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.SKULL, 1, 0), ColorUtils.getDye(EnumDyeColor.WHITE, 10));
        RecipeRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.SKULL, 1, 1), ColorUtils.getDye(EnumDyeColor.WHITE, 10));
        RecipeRegistry.MILLSTONE.addMillRecipe(new ItemStack(Blocks.BONE_BLOCK), ColorUtils.getDye(EnumDyeColor.WHITE, 9));

        RecipeRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.BEETROOT), ColorUtils.getDye(EnumDyeColor.RED, 2));
        RecipeRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.LEATHER), ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER));
        RecipeRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.RABBIT_HIDE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT));
        RecipeRegistry.MILLSTONE.addMillRecipe(ItemMaterial.getStack(ItemMaterial.EnumMaterial.LEATHER_CUT), ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT));
        for (BlockStateIngredient flower : ColorUtils.FLOWER_TO_DYES.keySet()) {
            RecipeRegistry.MILLSTONE.addMillRecipe(flower, ColorUtils.FLOWER_TO_DYES.get(flower).getStack());
        }
        RecipeRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage()), ItemMaterial.getStack(ItemMaterial.EnumMaterial.COCOA_POWDER));
        RecipeRegistry.MILLSTONE.addMillRecipe(new OreIngredient("cropWheat"), BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD));
        RecipeRegistry.MILLSTONE.addMillRecipe(new OreIngredient("cropBarley"), BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD));
        RecipeRegistry.MILLSTONE.addMillRecipe(new OreIngredient("cropOats"), BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD));
        RecipeRegistry.MILLSTONE.addMillRecipe(new OreIngredient("cropRye"), BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD));
        RecipeRegistry.MILLSTONE.addMillRecipe(new OreIngredient("cropRice"), BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD));
    }

}


