package betterwithmods.api.recipe.output.impl;

import betterwithmods.api.recipe.output.IOutput;
import betterwithmods.util.InvUtils;
import betterwithmods.util.TooltipLib;
import net.minecraft.item.ItemStack;

import java.util.Random;

import static betterwithmods.util.TooltipLib.RANDOM_OUTPUT;

public class RandomOutput extends StackOutput {
    private static final Random RANDOM = new Random();

    private final int min;
    private final int max;

    public RandomOutput(ItemStack stack, int min, int max) {
        super(stack);
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    private int rand() {
        return RANDOM.nextInt((max - min) + 1) + min;
    }

    public ItemStack getRandomStack() {
        return InvUtils.setCount(getOutput(), rand());
    }


    public RandomOutput copy() {
        return new RandomOutput(getOutput(), min, max);
    }

    public String getTooltip() {
        return TooltipLib.getTooltip(RANDOM_OUTPUT, min, max);
    }

    @Override
    public boolean equals(IOutput output) {
        if (output instanceof RandomOutput) {
            RandomOutput other = (RandomOutput) output;
            return this.getMax() == other.getMax() && this.getMin() == other.getMin() && InvUtils.matches(other.getOutput(), this.getOutput());
        }
        return false;
    }
}
