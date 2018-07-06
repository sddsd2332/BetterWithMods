package betterwithmods.api.recipe.output;

import net.minecraft.item.ItemStack;

public interface IOutput {

    ItemStack getOutput();

    String getTooltip();

    boolean equals(IOutput output);

    IOutput copy();
}
