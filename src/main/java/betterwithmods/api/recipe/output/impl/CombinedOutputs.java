package betterwithmods.api.recipe.output.impl;

import betterwithmods.api.recipe.output.IOutput;
import betterwithmods.api.recipe.output.IRecipeOutputs;
import betterwithmods.util.InvUtils;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;
import java.util.stream.Collectors;

public class CombinedOutputs implements IRecipeOutputs {

    private final List<IRecipeOutputs> recipeOutputs;

    public CombinedOutputs(IRecipeOutputs... recipeOutputs) {
        this(Lists.newArrayList(recipeOutputs));
    }

    public CombinedOutputs(List<IRecipeOutputs> recipeOutputs) {
        this.recipeOutputs = recipeOutputs;
    }

    @Override
    public NonNullList<ItemStack> getOutputs() {
        return InvUtils.asNonnullList(recipeOutputs.stream().map(IRecipeOutputs::getOutputs).flatMap(List::stream).collect(Collectors.toList()));
    }

    @Override
    public List<IOutput> getDisplayOutputs() {
        return InvUtils.asNonnullList(recipeOutputs.stream().map(IRecipeOutputs::getDisplayOutputs).flatMap(List::stream).collect(Collectors.toList()));
    }

    @Override
    public boolean matches(List<ItemStack> outputs) {
        return recipeOutputs.stream().allMatch( r -> r.matches(outputs));
    }

    @Override
    public boolean matchesFuzzy(List<ItemStack> outputs) {
        return recipeOutputs.stream().allMatch( r -> r.matchesFuzzy(outputs));
    }

    @Override
    public boolean isInvalid() {
        return recipeOutputs.stream().anyMatch(IRecipeOutputs::isInvalid);
    }
}
