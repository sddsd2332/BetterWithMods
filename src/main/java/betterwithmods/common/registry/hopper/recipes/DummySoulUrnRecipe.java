package betterwithmods.common.registry.hopper.recipes;

import betterwithmods.common.BWMBlocks;
import betterwithmods.util.InvUtils;
import betterwithmods.util.StackIngredient;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class DummySoulUrnRecipe extends SoulUrnRecipe {

    public DummySoulUrnRecipe(SoulUrnRecipe parent) {
        super(StackIngredient.fromIngredient(8, parent.input), parent.getOutputs(), parent.getSecondaryOutputs().stream().map(s -> InvUtils.setCount(s, 8)).collect(Collectors.toList()));
    }

    @Override
    public List<ItemStack> getInputContainer() {
        return Lists.newArrayList(new ItemStack(BWMBlocks.URN));
    }

    @Override
    public List<ItemStack> getOutputContainer() {
        return Lists.newArrayList(new ItemStack(BWMBlocks.SOUL_URN));
    }
}
