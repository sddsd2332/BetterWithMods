package betterwithmods.common.registry.block.managers;

import betterwithmods.common.registry.block.recipe.KilnRecipe;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import betterwithmods.lib.ModLib;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class CrafingManagerKiln extends CraftingManagerBlock<KilnRecipe> {

    public CrafingManagerKiln() {
        super(new ResourceLocation(ModLib.MODID, "kiln"), KilnRecipe.class);
    }

    public KilnRecipe addStokedRecipe(ItemStack input, ItemStack outputs) {
        return addRecipe(input, outputs, BWMHeatRegistry.STOKED_HEAT);
    }

    public KilnRecipe addStokedRecipe(ItemStack input, List<ItemStack> outputs) {
        return addRecipe(input, outputs, BWMHeatRegistry.STOKED_HEAT);
    }


    public KilnRecipe addUnstokedRecipe(ItemStack input, List<ItemStack> outputs) {
        return addRecipe(input, outputs, BWMHeatRegistry.UNSTOKED_HEAT);
    }

    public KilnRecipe addUnstokedRecipe(ItemStack input, ItemStack outputs) {
        return addRecipe(input, outputs, BWMHeatRegistry.UNSTOKED_HEAT);
    }

    public KilnRecipe addRecipe(ItemStack input, ItemStack outputs, int heat) {
        return addRecipe(input, Lists.newArrayList(outputs), heat);
    }

    public KilnRecipe addRecipe(ItemStack input, List<ItemStack> outputs, int heat) {
        KilnRecipe recipe = new KilnRecipe(new BlockStateIngredient(input), outputs, heat, 500 * heat);
        register(recipe);
        return recipe;
    }

    public KilnRecipe addRecipe(ItemStack input, List<ItemStack> outputs, int heat, int cookTime) {
        KilnRecipe recipe = new KilnRecipe(new BlockStateIngredient(input), outputs, heat, cookTime);
        register(recipe);
        return recipe;
    }

    public List<KilnRecipe> getRecipesForHeat(int heat) {
        return getDisplayRecipes().stream().filter(r -> r.getHeat() == heat).collect(Collectors.toList());
    }
}
