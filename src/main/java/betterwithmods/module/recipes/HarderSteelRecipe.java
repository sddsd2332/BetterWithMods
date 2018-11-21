package betterwithmods.module.recipes;

import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.block.recipe.builder.KilnRecipeBuilder;
import betterwithmods.common.registry.bulk.recipes.builder.CrucibleRecipeBuilder;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.module.internal.RecipeRegistry;
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
        CrucibleRecipeBuilder builder = new CrucibleRecipeBuilder();
        RecipeRegistry.CRUCIBLE.registerAll(
                builder.stoked()
                        .inputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.ENDER_SLAG))
                        .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.BRIMSTONE), ItemMaterial.getStack(ItemMaterial.EnumMaterial.SOUL_FLUX)).build(),
                builder.stoked().inputs(
                        new OreIngredient("blockSoulUrn"),
                        new OreIngredient("ingotIron"),
                        new OreIngredient("dustCarbon"),
                        Ingredient.fromStacks(ItemMaterial.getStack(ItemMaterial.EnumMaterial.SOUL_FLUX)))
                        .outputs(ItemMaterial.getStack(ItemMaterial.EnumMaterial.STEEL_INGOT)).build()
        );

        KilnRecipeBuilder builderKiln = new KilnRecipeBuilder();

        RecipeRegistry.KILN.register(builderKiln.stoked()
                .input(new ItemStack(Blocks.END_STONE))
                .outputs(BlockAesthetic.getStack(BlockAesthetic.Type.WHITECOBBLE, 1), ItemMaterial.getStack(ItemMaterial.EnumMaterial.ENDER_SLAG))
                .build());
    }

}
