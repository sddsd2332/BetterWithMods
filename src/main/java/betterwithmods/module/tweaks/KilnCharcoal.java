package betterwithmods.module.tweaks;

import betterwithmods.library.modularity.impl.Feature;
import betterwithmods.module.internal.RecipeRegistry;
import com.google.common.collect.Lists;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;


/**
 * Created by primetoxinz on 5/16/17.
 */
public class KilnCharcoal extends Feature {

    private static final ItemStack CHARCOAL = new ItemStack(Items.COAL, 1, 1);
    private boolean disableFurnaceCharcoal;

    @Override
    public String getDescription() {
        return "Add Charcoal smelting to the Kiln";
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        disableFurnaceCharcoal = loadProperty("Disable Furnace Charcoal", true).setComment("Remove recipes to make Charcoal in a Furnace").get();
        List<ItemStack> logs = Lists.newArrayList();
        logs.addAll(OreDictionary.getOres("logWood"));

        for (ItemStack stack : logs) {
            if (stack.getItem() instanceof ItemBlock) {
                ItemStack charcoalOutput = FurnaceRecipes.instance().getSmeltingResult(stack).copy();
                if (charcoalOutput.isEmpty())
                    continue;
                if (disableFurnaceCharcoal)
                    RecipeRegistry.removeFurnaceRecipe(stack);
                RecipeRegistry.KILN.addStokedRecipe(stack, charcoalOutput);
            }
        }
    }


}
