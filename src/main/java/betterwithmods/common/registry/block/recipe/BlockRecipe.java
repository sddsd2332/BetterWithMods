package betterwithmods.common.registry.block.recipe;

import betterwithmods.api.recipe.output.IRecipeOutputs;
import betterwithmods.library.utils.InventoryUtils;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Random;

/**
 * Purpose:
 *
 * @author primetoxinz
 * @version 03/19/2018
 */
public abstract class BlockRecipe<V extends IForgeRegistryEntry<V>> extends IForgeRegistryEntry.Impl<V> {
    private final BlockStateIngredient input;
    private final IRecipeOutputs recipeOutput;

    public BlockRecipe(BlockStateIngredient input, IRecipeOutputs recipeOutput) {
        this.input = input;
        this.recipeOutput = recipeOutput;
    }

    public abstract boolean craftRecipe(World world, BlockPos pos, Random rand, IBlockState state);

    public NonNullList<ItemStack> onCraft(World world, BlockPos pos) {
        NonNullList<ItemStack> items = NonNullList.create();
        if (consumeIngredients(world, pos)) {
            items.addAll(getOutputs());
        }
        return items;
    }

    public boolean consumeIngredients(World world, BlockPos pos) {
        return world.setBlockToAir(pos);
    }

    public BlockStateIngredient getInput() {
        return input;
    }

    public IRecipeOutputs getRecipeOutput() {
        return recipeOutput;
    }

    public NonNullList<ItemStack> getOutputs() {
        return recipeOutput.getOutputs();
    }

    @Override
    public String toString() {
        return String.format("%s-> %s", input, getOutputs());
    }

    public boolean isInvalid() {
        return (input.isSimple() && InventoryUtils.isIngredientValid(input) || recipeOutput.isInvalid());
    }

    public boolean matches(World world, BlockPos pos, IBlockState state) {
        return getInput().test(world, pos, state);
    }

    public boolean isHidden() {
        return false;
    }
}