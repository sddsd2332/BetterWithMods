package betterwithmods.module.hardcore.crafting;

import betterwithmods.common.items.ItemMaterial;
import betterwithmods.library.modularity.impl.Feature;
import betterwithmods.library.recipes.RecipeMatchers;
import betterwithmods.library.recipes.RecipeRemover;
import betterwithmods.library.utils.ingredient.StackIngredient;
import betterwithmods.module.internal.RecipeRegistry;
import betterwithmods.module.recipes.AnvilRecipes;
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
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:dispenser"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:dropper"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:iron_door"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:iron_trapdoor"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:lever"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:piston"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:tripwire_hook"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:wooden_button"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:wooden_pressure_plate"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:stone_button"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:stone_pressure_plate"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:repeater"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:heavy_weighted_pressure_plate"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:light_weighted_pressure_plate"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:comparator"));
        RecipeRegistry.removeRecipe(new RecipeRemover<>(RecipeMatchers.REGISTRY_STRING, "minecraft:observer"));

    }


    @Override
    public void onInit(FMLInitializationEvent event) {
        RecipeRegistry.CRUCIBLE.addStokedRecipe(StackIngredient.fromStacks(new ItemStack(Blocks.IRON_TRAPDOOR, 2)), new ItemStack(Items.IRON_INGOT, 4));
        //New observer recipe :)
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation("betterwithmods", "observer"), new ItemStack(Blocks.OBSERVER), "LSSL", "SRRS", "STTS", 'S', "stone", 'R', "dustRedstone", 'T', Blocks.REDSTONE_TORCH, 'L', ItemMaterial.getStack(ItemMaterial.EnumMaterial.POLISHED_LAPIS));
    }

    //TODO
//    @Override
//    public void disabledInit(FMLInitializationEvent event) {
//        BWRegistry.CRUCIBLE.addStokedRecipe(StackIngredient.fromStacks(new ItemStack(Blocks.IRON_TRAPDOOR, 2)), new ItemStack(Items.IRON_INGOT, 6));
//    }
}
