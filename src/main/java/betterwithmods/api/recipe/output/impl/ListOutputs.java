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

public class ListOutputs implements IRecipeOutputs {
    protected final List<StackOutput> outputs;

    public ListOutputs(ItemStack... outputs) {
        this(Lists.newArrayList(outputs));
    }

    public ListOutputs(List<ItemStack> outputs) {
        this.outputs = outputs.stream().filter(s -> !s.isEmpty()).map(StackOutput::new).collect(Collectors.toList());
    }

    @Override
    public NonNullList<ItemStack> getOutputs() {
        return ListUtils.asNonnullList(this.outputs.stream().map(StackOutput::getOutput).map(ItemStack::copy).collect(Collectors.toList()));
    }

    @Override
    public List<IOutput> getDisplayOutputs() {
        return cast(outputs);
    }

    @Override
    public boolean matches(List<ItemStack> outputs) {
        return InventoryUtils.matchesExact(getOutputs(), outputs);
    }

    @Override
    public boolean matchesFuzzy(List<ItemStack> outputs) {
        return InventoryUtils.matches(getOutputs(), outputs);
    }

    @Override
    public boolean isInvalid() {
        return outputs.isEmpty();
    }


}
