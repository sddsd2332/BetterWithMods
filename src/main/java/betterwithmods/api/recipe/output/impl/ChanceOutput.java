package betterwithmods.api.recipe.output.impl;

import betterwithmods.api.recipe.output.IOutput;
import betterwithmods.util.InvUtils;
import betterwithmods.util.TooltipLib;
import net.minecraft.item.ItemStack;

import static betterwithmods.util.TooltipLib.CHANCE_OUTPUT;

public class ChanceOutput extends StackOutput {
    private final double weight;

    public ChanceOutput(ItemStack stack, double weight) {
        super(stack);
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String getTooltip() {
        return TooltipLib.getTooltip(CHANCE_OUTPUT, weight * 100);
    }

    @Override
    public boolean equals(IOutput output) {
        if (output instanceof ChanceOutput) {
            ChanceOutput other = (ChanceOutput) output;
            return other.getWeight() == this.getWeight() && InvUtils.matches(other.getOutput(), this.getOutput());
        }
        return false;
    }
}
