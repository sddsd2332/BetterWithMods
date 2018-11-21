package betterwithmods.common.registry.block.recipe.builder;

import betterwithmods.api.recipe.output.IRecipeOutputs;
import betterwithmods.api.recipe.output.impl.ListOutputs;
import betterwithmods.common.registry.block.recipe.BlockRecipe;
import betterwithmods.library.common.recipes.BaseRecipeBuilder;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import com.google.common.base.Preconditions;
import net.minecraft.item.ItemStack;

public abstract class BlockRecipeBuilder<V extends BlockRecipe<V>> extends BaseRecipeBuilder<V> {


    protected BlockStateIngredient input;
    protected IRecipeOutputs outputs;
    protected int priority;


    public BlockRecipeBuilder<V> input(ItemStack... input) {
        return input(new BlockStateIngredient(input));
    }

    public BlockRecipeBuilder<V> input(BlockStateIngredient input) {
        this.input = input;
        return this;
    }

    public BlockRecipeBuilder<V> outputs(ItemStack... stacks) {
        return outputs(new ListOutputs(stacks));
    }


    public BlockRecipeBuilder<V> outputs(IRecipeOutputs outputs) {
        this.outputs = outputs;
        return this;
    }

    @Override
    public void reset() {
        input = null;
        outputs = null;
    }

    @Override
    public V build() {
        Preconditions.checkNotNull(input, "inputs");
        Preconditions.checkNotNull(outputs, "outputs");

        V recipe = create();

        if (name != null) {
            recipe.setRegistryName(name);
        }
        reset();
        return recipe;
    }

}
