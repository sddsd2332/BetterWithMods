package betterwithmods.common.registry.bulk.recipes;

import betterwithmods.api.recipe.output.IRecipeOutputs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nonnull;
import java.util.List;

public class CrucibleRecipe extends CookingPotRecipe<CrucibleRecipe> {

    public CrucibleRecipe(@Nonnull List<Ingredient> inputs, @Nonnull List<ItemStack> outputs, int heat) {
        super(inputs, outputs, heat);
    }

    public CrucibleRecipe(List<Ingredient> inputs, IRecipeOutputs outputs, int heat) {
        super(inputs, outputs, heat);
    }
}