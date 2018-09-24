package betterwithmods.common;

import betterwithmods.library.utils.InventoryUtils;
import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.List;
import java.util.regex.Pattern;

public final class BWMRecipes {

    public static final List<ItemStack> REMOVE_RECIPE_BY_OUTPUT = Lists.newArrayList();
    public static final List<List<Ingredient>> REMOVE_RECIPE_BY_INPUT = Lists.newArrayList();
    public static final List<ResourceLocation> REMOVE_RECIPE_BY_RL = Lists.newArrayList();
    public static final List<Pattern> REMOVE_BY_REGEX = Lists.newArrayList();

    public static void addRecipe(IRecipe recipe) {
        ForgeRegistries.RECIPES.register(recipe);
    }

    public static void removeRecipe(Ingredient... inputs) {
        REMOVE_RECIPE_BY_INPUT.add(Lists.newArrayList(inputs));
    }

    public static void removeRecipe(ItemStack output) {
        REMOVE_RECIPE_BY_OUTPUT.add(output);
    }

    public static void removeRecipe(ResourceLocation loc) {
        REMOVE_RECIPE_BY_RL.add(loc);
    }

    public static void removeRecipe(IRecipe recipe) {
        ForgeRegistry<IRecipe> registry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;
        registry.remove(recipe.getRegistryName());
    }

    public static void removeRecipe(String loc) {
        removeRecipe(new ResourceLocation(loc));
    }

    public static void removeRecipe(Pattern pattern) {
        REMOVE_BY_REGEX.add(pattern);
    }

    // Replace calls to GameRegistry.addShapeless/ShapedRecipe with these methods, which will dump it to a json in your dir of choice
// Also works with OD, replace GameRegistry.addRecipe(new ShapedOreRecipe/ShapelessOreRecipe with the same calls

    public static void addFurnaceRecipe(ItemStack input, ItemStack output) {
        FurnaceRecipes.instance().getSmeltingList().put(input, output);
    }

    public static void removeFurnaceRecipe(Item input) {
        removeFurnaceRecipe(new ItemStack(input));
    }

    public static boolean removeFurnaceRecipe(ItemStack input) {
        //for some reason mojang put fucking wildcard for their ore meta
        return FurnaceRecipes.instance().getSmeltingList().entrySet().removeIf(next -> InventoryUtils.matches(next.getKey(), input));
    }


}
