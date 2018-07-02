package betterwithmods.module.hardcore.crafting;

import betterwithmods.common.BWMRecipes;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.Feature;
import betterwithmods.util.StackIngredient;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class HCRedstone extends Feature {
    public static boolean stoneDeviceRecipesAnvil;

    @Override
    public void setupConfig() {
        stoneDeviceRecipesAnvil = loadPropBool("Stone Device Recipes Require Anvil", "Makes it so stone buttons and pressure plates require cut stone, which must be done in the anvil", true);

    }

    @Override
    public String getFeatureDescription() {
        return "Changes the recipes for Redstone devices to be more complex";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
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
        BWMRecipes.removeRecipe("minecraft:hopper");
        BWMRecipes.removeRecipe("minecraft:comparator");
        BWMRecipes.removeRecipe("minecraft:observer");
    }


    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        ItemStack LATCH = ItemMaterial.getStack(ItemMaterial.EnumMaterial.REDSTONE_LATCH);
        if (!stoneDeviceRecipesAnvil) {
            event.getRegistry().register(new ShapedOreRecipe(null, Blocks.STONE_BUTTON, "S", "R", 'S', "stone", 'R', LATCH).setRegistryName(new ResourceLocation("betterwithmods", "stone_button")));
            event.getRegistry().register(new ShapedOreRecipe(null, Blocks.STONE_PRESSURE_PLATE, "SS", "RR", 'S', "stone", 'R', LATCH).setRegistryName(new ResourceLocation("betterwithmods", "stone_pressure_plate")));
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {

        BWRegistry.CRUCIBLE.addStokedRecipe(StackIngredient.fromStacks(new ItemStack(Blocks.IRON_TRAPDOOR, 2)), new ItemStack(Items.IRON_INGOT, 4));

    }

    @Override
    public void disabledInit(FMLInitializationEvent event) {
        BWRegistry.CRUCIBLE.addStokedRecipe(StackIngredient.fromStacks(new ItemStack(Blocks.IRON_TRAPDOOR, 2)), new ItemStack(Items.IRON_INGOT, 6));
    }
}
