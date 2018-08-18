package betterwithmods.module.gameplay;

import betterwithmods.api.recipe.output.IRecipeOutputs;
import betterwithmods.api.recipe.output.impl.ChanceOutputs;
import betterwithmods.api.recipe.output.impl.CombinedOutputs;
import betterwithmods.api.recipe.output.impl.ListOutputs;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import betterwithmods.util.StackIngredient;
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
    public static double urnReturnChance;


    @Override
    public String getDescription() {
        return "Whether Steel requires End Slag, a material only available after the End.";
    }


    @Override
    public void onInit(FMLInitializationEvent event) {

        urnReturnChance = loadProperty("Urn Return Chance", 0.75f).setComment("Percent chance (0.0-1.0) that the urn is returned when creating the steel.").get();

        BWRegistry.CRUCIBLE.addStokedRecipe(StackIngredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.ENDER_SLAG)), Lists.newArrayList(ItemMaterial.getStack(ItemMaterial.EnumMaterial.BRIMSTONE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.SOUL_FLUX)));
        BWRegistry.CRUCIBLE.addStokedRecipe(Lists.newArrayList(new OreIngredient("blockSoulUrn"), new OreIngredient("ingotIron"), new OreIngredient("dustCarbon"), Ingredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SOUL_FLUX))), getOutputs());
        BWRegistry.KILN.addStokedRecipe(new ItemStack(Blocks.END_STONE), Lists.newArrayList(BlockAesthetic.getStack(BlockAesthetic.EnumType.WHITECOBBLE, 1), ItemMaterial.getStack(ItemMaterial.EnumMaterial.ENDER_SLAG)));
    }
    //TODO disabledInit :reee:

//    @Override
//    public void disabledInit(FMLInitializationEvent event) {
//
//
//        BWRegistry.CRUCIBLE.addStokedRecipe(Ingredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.ENDER_SLAG)), Lists.newArrayList(ItemMaterial.getStack(ItemMaterial.EnumMaterial.BRIMSTONE)));
//        BWRegistry.CRUCIBLE.addStokedRecipe(Lists.newArrayList(new OreIngredient("blockSoulUrn"), new OreIngredient("ingotIron"), new OreIngredient("dustCarbon")), getOutputs());
//        BWRegistry.KILN.addStokedRecipe(new ItemStack(Blocks.END_STONE), Lists.newArrayList(BlockAesthetic.getStack(BlockAesthetic.EnumType.WHITECOBBLE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.BRIMSTONE)));
//    }

    public IRecipeOutputs getOutputs() {
        return new CombinedOutputs(new ListOutputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT)), new ChanceOutputs(new ItemStack(BWMBlocks.URN, 1, 0), urnReturnChance));
    }
}
