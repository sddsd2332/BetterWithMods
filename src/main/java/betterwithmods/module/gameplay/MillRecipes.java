package betterwithmods.module.gameplay;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMRecipes;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.BWSounds;
import betterwithmods.common.blocks.BlockRawPastry;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.block.recipe.BlockIngredient;
import betterwithmods.module.Feature;
import betterwithmods.util.ColorUtils;
import betterwithmods.util.StackIngredient;
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


    public MillRecipes() {
        canDisable = false;
    }


    @Override
    public void setupConfig() {
        grindingOnly = loadPropBool("Grinding Only", "Remove normal recipes for certain grindable items", true);
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        super.onPreInit(event);
        if (grindingOnly) {
            BWMRecipes.removeRecipe(new ItemStack(Items.SUGAR));
            BWMRecipes.removeRecipe(new ItemStack(Items.BLAZE_POWDER));
            BWMRecipes.removeRecipe(StackIngredient.fromStacks(new ItemStack(Items.BEETROOT)));
            for (BlockIngredient flower : ColorUtils.FLOWER_TO_DYES.keySet()) {
                BWMRecipes.removeRecipe(flower);
            }
        }
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        BWRegistry.MILLSTONE.addMillRecipe(new OreIngredient("netherrack"), Lists.newArrayList(ItemMaterial.getStack(ItemMaterial.EnumMaterial.GROUND_NETHERRACK)), BWSounds.MILLSTONE_NETHERRACK);
        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.BLAZE_ROD), Lists.newArrayList(new ItemStack(Items.BLAZE_POWDER, 3)), SoundEvents.ENTITY_BLAZE_DEATH);

        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(BWMBlocks.WOLF), Lists.newArrayList(new ItemStack(Items.STRING, 10), ColorUtils.getDye(EnumDyeColor.RED, 3)), SoundEvents.ENTITY_WOLF_WHINE);
        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.REEDS), Lists.newArrayList(new ItemStack(Items.SUGAR, 2)));

        BWRegistry.MILLSTONE.addMillRecipe(new OreIngredient("cropHemp"), ItemMaterial.getStack(ItemMaterial.EnumMaterial.HEMP_FIBERS, 3));
        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.COAL, 1), ItemMaterial.getStack(ItemMaterial.EnumMaterial.COAL_DUST));
        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.COAL, 1, 1), ItemMaterial.getStack(ItemMaterial.EnumMaterial.CHARCOAL_DUST));
        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.BONE), ColorUtils.getDye(EnumDyeColor.WHITE, 6));
        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.SKULL, 1, 0), ColorUtils.getDye(EnumDyeColor.WHITE, 10));
        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.SKULL, 1, 1), ColorUtils.getDye(EnumDyeColor.WHITE, 10));
        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(Blocks.BONE_BLOCK), ColorUtils.getDye(EnumDyeColor.WHITE, 9));

        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.BEETROOT), ColorUtils.getDye(EnumDyeColor.RED, 2));
        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.LEATHER), ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER));
        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.RABBIT_HIDE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT));
        BWRegistry.MILLSTONE.addMillRecipe(ItemMaterial.getStack(ItemMaterial.EnumMaterial.LEATHER_CUT), ItemMaterial.getStack(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT));
        for (BlockIngredient flower : ColorUtils.FLOWER_TO_DYES.keySet()) {
            BWRegistry.MILLSTONE.addMillRecipe(flower, ColorUtils.FLOWER_TO_DYES.get(flower).getStack());
        }
        BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage()), ItemMaterial.getStack(ItemMaterial.EnumMaterial.COCOA_POWDER));
        BWRegistry.MILLSTONE.addMillRecipe(new OreIngredient("cropWheat"), BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD));
        BWRegistry.MILLSTONE.addMillRecipe(new OreIngredient("cropBarley"), BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD));
        BWRegistry.MILLSTONE.addMillRecipe(new OreIngredient("cropOats"), BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD));
        BWRegistry.MILLSTONE.addMillRecipe(new OreIngredient("cropRye"), BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD));
        BWRegistry.MILLSTONE.addMillRecipe(new OreIngredient("cropRice"), BlockRawPastry.getStack(BlockRawPastry.EnumType.BREAD));
    }
}


