package betterwithmods.module.hardcore.crafting;

import betterwithmods.common.BWMRecipes;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import betterwithmods.module.gameplay.AnvilRecipes;
import betterwithmods.util.StackIngredient;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by primetoxinz on 4/20/17.
 */

@Mod.EventBusSubscriber
public class HCRedstone extends Feature {

    @Override
    public String getDescription() {
        return "Changes the recipes for Redstone devices to be more complex";
    }

    @Override
    public void onPreInit(FMLPreInitializationEvent event) {
        BWMRecipes.removeRecipe("minecraft:dispenser");
        BWMRecipes.removeRecipe("minecraft:dropper");
        BWMRecipes.removeRecipe("minecraft:iron_door");
        BWMRecipes.removeRecipe("minecraft:iron_trapdoor");
        BWMRecipes.removeRecipe("minecraft:lever");
        BWMRecipes.removeRecipe("minecraft:piston");
        BWMRecipes.removeRecipe("minecraft:tripwire_hook");
        BWMRecipes.removeRecipe("minecraft:wooden_button");
        BWMRecipes.removeRecipe("minecraft:wooden_pressure_plate");
        BWMRecipes.removeRecipe("minecraft:stone_button");
        BWMRecipes.removeRecipe("minecraft:stone_pressure_plate");
        BWMRecipes.removeRecipe("minecraft:repeater");
        BWMRecipes.removeRecipe("minecraft:heavy_weighted_pressure_plate");
        BWMRecipes.removeRecipe("minecraft:light_weighted_pressure_plate");
        BWMRecipes.removeRecipe("minecraft:comparator");
        BWMRecipes.removeRecipe("minecraft:observer");
    }


    @Override
    public void onInit(FMLInitializationEvent event) {
        BWRegistry.CRUCIBLE.addStokedRecipe(StackIngredient.fromStacks(new ItemStack(Blocks.IRON_TRAPDOOR, 2)), new ItemStack(Items.IRON_INGOT, 4));
        //New observer recipe :)
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation("betterwithmods", "observer"), new ItemStack(Blocks.OBSERVER), "LSSL", "SRRS", "STTS", 'S', "stone", 'R', "dustRedstone", 'T', Blocks.REDSTONE_TORCH, 'L', ItemMaterial.getStack(ItemMaterial.EnumMaterial.POLISHED_LAPIS));
    }

    //TODO
//    @Override
//    public void disabledInit(FMLInitializationEvent event) {
//        BWRegistry.CRUCIBLE.addStokedRecipe(StackIngredient.fromStacks(new ItemStack(Blocks.IRON_TRAPDOOR, 2)), new ItemStack(Items.IRON_INGOT, 6));
//    }
}
