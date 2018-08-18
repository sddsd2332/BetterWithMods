package betterwithmods.module.tweaks;

import betterwithmods.common.BWMOreDictionary;
import betterwithmods.common.BWMRegistry;
import betterwithmods.module.Feature;
import betterwithmods.util.InvUtils;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import java.util.Arrays;

/**
 * Created by primetoxinz on 4/20/17.
 */
@Mod.EventBusSubscriber
public class KilnSmelting extends Feature {
    private static int oreProductionCount;


    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        oreProductionCount = loadProperty("Ore Production Count", 1).setComment("Number of Materials returned from Smelting an Ore in the Kiln").get();

        BWMOreDictionary.oreNames.stream().flatMap(ore -> Arrays.stream(ore.getMatchingStacks())).filter(s -> s.getItem() instanceof ItemBlock).forEach(input -> {
            ItemStack output = FurnaceRecipes.instance().getSmeltingResult(input).copy();
            BWMRegistry.KILN.addStokedRecipe(input, InvUtils.setCount(output, oreProductionCount));
        });
    }

    @Override
    public String getDescription() {
        return "Allows Kiln to Smelt Ores";
    }
}
