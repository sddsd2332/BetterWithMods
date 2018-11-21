package betterwithmods.module.tweaks;

import betterwithmods.common.BWMOreDictionary;
import betterwithmods.common.registry.block.recipe.builder.KilnRecipeBuilder;
import betterwithmods.library.common.modularity.impl.Feature;
import betterwithmods.library.utils.InventoryUtils;
import betterwithmods.module.internal.RecipeRegistry;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.util.Arrays;

/**
 * Created by primetoxinz on 4/20/17.
 */


public class KilnSmelting extends Feature {
    private static int oreProductionCount;


    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        oreProductionCount = loadProperty("Ore Production Count", 1).setComment("Number of Materials returned from Smelting an Ore in the Kiln").get();

        KilnRecipeBuilder builder = new KilnRecipeBuilder();

        BWMOreDictionary.oreNames.stream().flatMap(ore -> Arrays.stream(ore.getMatchingStacks())).filter(s -> s.getItem() instanceof ItemBlock).forEach(input -> {
            ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input).copy();
            RecipeRegistry.KILN.register(builder.stoked().input(input).outputs(InventoryUtils.setCount(output, oreProductionCount)).build());
        });
    }

    @Override
    public String getDescription() {
        return "Allows Kiln to Smelt Ores";
    }
}
