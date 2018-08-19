package betterwithmods.module.tweaks;

import betterwithmods.common.BWMRegistry;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class SaddleRecipe extends Feature {
    public SaddleRecipe() {

    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        BWMRegistry.CAULDRON.addStokedRecipe(Ingredient.fromStacks(new ItemStack(Items.SADDLE)), ItemMaterial.getStack(ItemMaterial.EnumMaterial.GLUE, 2));
    }

    @Override
    public String getDescription() {
        return "Add recipe for creating saddles from tanned leather and a stoked cauldron recipe to turn it into glue";
    }
}
