package betterwithmods.common;

import betterwithmods.util.InvUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.*;
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
        return FurnaceRecipes.instance().getSmeltingList().entrySet().removeIf(next -> InvUtils.matches(next.getKey(), input));
    }

    public static Set<IBlockState> getStatesFromStack(ItemStack stack) {
        if (stack.getItem() instanceof ItemBlock) {
            if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
                return Sets.newHashSet(((ItemBlock) stack.getItem()).getBlock().getBlockState().getValidStates());
            }
            return Sets.newHashSet(getStateFromStack(stack));
        }
        return Sets.newHashSet();
    }

    public static IBlockState getStateFromStack(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemBlock) {
            final ItemBlock itemBlock = ((ItemBlock) stack.getItem());
            return itemBlock.getBlock().getStateFromMeta(itemBlock.getMetadata(stack));
        }
        return null;
    }

    public static ItemStack getStackFromState(IBlockState state) {
        if (state == null)
            return ItemStack.EMPTY;
        Block block = state.getBlock();
        int meta = block.damageDropped(state);
        return new ItemStack(block, 1, meta);
    }


}
