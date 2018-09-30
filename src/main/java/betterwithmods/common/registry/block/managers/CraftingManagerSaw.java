package betterwithmods.common.registry.block.managers;

import betterwithmods.library.utils.ingredient.blockstate.BlockDropIngredient;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import betterwithmods.common.registry.block.recipe.SawRecipe;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;

import java.util.List;

public class CraftingManagerSaw extends CraftingManagerBlock<SawRecipe> {

    public SawRecipe addRecipe(ItemStack input, ItemStack outputs) {
        return addRecipe(input, Lists.newArrayList(outputs));
    }

    public SawRecipe addRecipe(ItemStack input, List<ItemStack> outputs) {
        return addRecipe(new SawRecipe(new BlockStateIngredient(input), outputs));
    }

    public SawRecipe addRecipe(BlockStateIngredient input, ItemStack outputs) {
        return addRecipe(input, Lists.newArrayList(outputs));
    }

    public SawRecipe addRecipe(BlockStateIngredient input, List<ItemStack> outputs) {
        return addRecipe(new SawRecipe(input, outputs));
    }

    public SawRecipe addSelfdropRecipe(ItemStack stack) {
        return addRecipe(new BlockDropIngredient(stack), Lists.newArrayList(stack));
    }

}
