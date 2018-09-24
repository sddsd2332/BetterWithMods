package betterwithmods.api.recipe.output.impl;

import betterwithmods.api.recipe.output.IOutput;
import betterwithmods.api.recipe.output.IRecipeOutputs;
import betterwithmods.library.utils.InventoryUtils;
import betterwithmods.library.utils.ListUtils;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.List;
import java.util.stream.Collectors;

public class RandomCountOutputs implements IRecipeOutputs {

    private final List<RandomOutput> outputs;
    private final List<ItemStack> itemStackList;

    public RandomCountOutputs(RandomOutput... outputs) {
        this(Lists.newArrayList(outputs));
    }

    public RandomCountOutputs(List<RandomOutput> outputs) {
        this.outputs = outputs;
        this.itemStackList = outputs.stream().map(RandomOutput::getOutput).collect(Collectors.toList());
    }

    @Override
    public List<IOutput> getDisplayOutputs() {
        return cast(outputs);
    }

    @Override
    public NonNullList<ItemStack> getOutputs() {
        return findResult();
    }

    @Override
    public boolean matches(List<ItemStack> outputs) {
        return InventoryUtils.matchesExact(outputs, itemStackList);
    }

    @Override
    public boolean matchesFuzzy(List<ItemStack> outputs) {
        return InventoryUtils.matches(outputs, itemStackList);
    }

    @Override
    public boolean isInvalid() {
        return outputs.isEmpty();
    }

    private NonNullList<ItemStack> findResult() {
        return ListUtils.asNonnullList(outputs.stream().map(RandomOutput::getRandomStack).collect(Collectors.toList()));
    }

}
