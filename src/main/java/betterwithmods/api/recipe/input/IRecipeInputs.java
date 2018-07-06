package betterwithmods.api.recipe.input;

import betterwithmods.api.recipe.output.IRecipeOutputs;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

public interface IRecipeInputs {

    int orderedMatch(IRecipeContext context);

    boolean match(IRecipeContext context);

    IRecipeOutputs consume(IRecipeContext context);

    boolean isInvalid();

    NonNullList<Ingredient> getInputs();

    default boolean handleContainers() {
        return true;
    }
}
