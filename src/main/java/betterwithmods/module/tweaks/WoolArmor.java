package betterwithmods.module.tweaks;

import betterwithmods.common.BWMItems;
import betterwithmods.common.BWMRecipes;
import betterwithmods.common.registry.crafting.RecipeArmorDye;
import betterwithmods.library.modularity.impl.Feature;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class WoolArmor extends Feature {


    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        BWMRecipes.addRecipe(new RecipeArmorDye(Ingredient.fromItems(BWMItems.WOOL_HELMET, BWMItems.WOOL_CHEST, BWMItems.WOOL_PANTS, BWMItems.WOOL_BOOTS)));
    }

    @Override
    public String getDescription() {
        return "Add Wool Armor";
    }
}
