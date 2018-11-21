package betterwithmods.module.recipes;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.registry.bulk.recipes.builder.CauldronRecipeBuilder;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.utils.ingredient.StackIngredient;
import betterwithmods.module.internal.RecipeRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.OreIngredient;

/**
 * Created by primetoxinz on 6/25/17.
 */
public class NetherGrowth extends Feature {

    @Override
    public void onInit(FMLInitializationEvent event) {

        CauldronRecipeBuilder builder = new CauldronRecipeBuilder();

        RecipeRegistry.CAULDRON.register(builder.unstoked().inputs(
                StackIngredient.fromStacks(new ItemStack(Blocks.BROWN_MUSHROOM)),
                StackIngredient.fromStacks(new ItemStack(Blocks.RED_MUSHROOM)),
                StackIngredient.fromStacks(new ItemStack(Blocks.MYCELIUM)),
                new OreIngredient("cropNetherWart"),
                StackIngredient.fromStacks(new ItemStack(Items.ROTTEN_FLESH)),
                StackIngredient.fromOre(8, "blockSoulUrn")
                ).outputs(new ItemStack(BWMBlocks.NETHER_GROWTH)).build()
        );
    }

    @Override
    public String getDescription() {
        return "Adds Nether Growth, a fungus that will *infest* the Nether and stop all mobs from spawning. Be sure before placing it!";
    }
}
