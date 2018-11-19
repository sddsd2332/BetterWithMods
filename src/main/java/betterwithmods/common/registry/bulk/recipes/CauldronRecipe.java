package betterwithmods.common.registry.bulk.recipes;

import betterwithmods.api.recipe.output.IRecipeOutputs;
import betterwithmods.api.tile.IHeatRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nonnull;
import java.util.List;

public class CauldronRecipe extends CookingPotRecipe<CauldronRecipe> {

    public CauldronRecipe(@Nonnull List<Ingredient> inputs, @Nonnull List<ItemStack> outputs, int heat) {
        super(inputs, outputs, heat);
    }

    public CauldronRecipe(List<Ingredient> inputs, IRecipeOutputs outputs, int heat) {
        super(inputs, outputs, heat);
    }
}