package betterwithmods.module.recipes;

import betterwithmods.api.recipe.output.impl.ListOutputs;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.library.modularity.impl.Feature;
import betterwithmods.library.utils.ingredient.StackIngredient;
import betterwithmods.module.internal.RecipeRegistry;
import com.google.common.collect.Lists;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.OreIngredient;

/**
 * Created by primetoxinz on 5/10/17.
 */
public class HarderSteelRecipe extends Feature {


    @Override
    public String getDescription() {
        return "Whether Steel requires End Slag, a material only available after the End.";
    }


    @Override
    public void onInit(FMLInitializationEvent event) {
        RecipeRegistry.CRUCIBLE.addStokedRecipe(StackIngredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.ENDER_SLAG)), Lists.newArrayList(ItemMaterial.getStack(ItemMaterial.EnumMaterial.BRIMSTONE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.SOUL_FLUX)));
        RecipeRegistry.CRUCIBLE.addStokedRecipe(Lists.newArrayList(new OreIngredient("blockSoulUrn"), new OreIngredient("ingotIron"), new OreIngredient("dustCarbon"), Ingredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SOUL_FLUX))), new ListOutputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT)));
        RecipeRegistry.KILN.addStokedRecipe(new ItemStack(Blocks.END_STONE), Lists.newArrayList(BlockAesthetic.getStack(BlockAesthetic.EnumType.WHITECOBBLE, 1), ItemMaterial.getStack(ItemMaterial.EnumMaterial.ENDER_SLAG)));
    }

}
