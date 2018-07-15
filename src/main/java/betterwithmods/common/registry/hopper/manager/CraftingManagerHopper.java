package betterwithmods.common.registry.hopper.manager;


import betterwithmods.common.registry.base.CraftingManagerBase;
import betterwithmods.common.registry.hopper.recipes.DummySoulUrnRecipe;
import betterwithmods.common.registry.hopper.recipes.HopperRecipe;
import betterwithmods.common.registry.hopper.recipes.SoulUrnRecipe;
import betterwithmods.common.tile.TileFilteredHopper;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CraftingManagerHopper extends CraftingManagerBase<HopperRecipe> {

    @Override
    public List<HopperRecipe> getDisplayRecipes() {
        List<HopperRecipe> display = Lists.newArrayList();
        for (HopperRecipe recipe : recipes) {
            display.add(recipe);
            if (recipe instanceof SoulUrnRecipe) {
                display.add(new DummySoulUrnRecipe((SoulUrnRecipe) recipe));
            }
        }
        return display;
    }

    @Override
    public HopperRecipe addRecipe(@Nonnull HopperRecipe recipe) {
        recipes.add(recipe);
        return recipe;
    }

    public Optional<HopperRecipe> findRecipe(TileFilteredHopper hopper, ItemStack input) {
        return recipes.stream().filter(r -> r.matches(hopper.getHopperFilter().getName(), input)).findFirst();
    }

    protected List<HopperRecipe> findRecipes(List<ItemStack> outputs, List<ItemStack> secondary) {
        List<HopperRecipe> recipes = findRecipeExact(outputs, secondary);
        if (recipes.isEmpty())
            recipes = findRecipeFuzzy(outputs, secondary);
        return recipes;
    }

    public List<HopperRecipe> findRecipeFuzzy(List<ItemStack> outputs, List<ItemStack> secondary) {
        return recipes.stream().filter(recipe -> recipe.getRecipeOutputInsert().matchesFuzzy(outputs) && recipe.getRecipeOutputWorld().matchesFuzzy(secondary)).collect(Collectors.toList());
    }

    public List<HopperRecipe> findRecipeExact(List<ItemStack> outputs, List<ItemStack> secondary) {
        return recipes.stream().filter(recipe -> recipe.getRecipeOutputInsert().matches(outputs) && recipe.getRecipeOutputWorld().matches(secondary)).collect(Collectors.toList());
    }

    public List<HopperRecipe> findRecipeByInput(ItemStack input) {
        return recipes.stream().filter(r -> r.getInputs().apply(input)).collect(Collectors.toList());
    }

    public boolean remove(List<ItemStack> outputs, List<ItemStack> secondary) {
        return recipes.removeAll(findRecipes(outputs, secondary));
    }

    public boolean removeFuzzy(List<ItemStack> outputs, List<ItemStack> secondary) {
        return recipes.removeAll(findRecipeFuzzy(outputs, secondary));
    }

    public boolean removeExact(List<ItemStack> outputs, List<ItemStack> secondary) {
        return recipes.removeAll(findRecipeExact(outputs, secondary));
    }

    public boolean removeByInput(ItemStack input) {
        return recipes.removeAll(findRecipeByInput(input));
    }


}
