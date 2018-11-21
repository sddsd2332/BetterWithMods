package betterwithmods.common.registry.block.recipe;

import betterwithmods.api.recipe.output.IRecipeOutputs;
import betterwithmods.common.event.FakePlayerHandler;
import betterwithmods.common.registry.block.managers.CraftingManagerTurntable;
import betterwithmods.common.tile.TileTurntable;
import betterwithmods.library.utils.InventoryUtils;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Optional;
import java.util.Random;

/**
 * Created by primetoxinz on 4/20/17.
 */
public class TurntableRecipe extends BlockRecipe<TurntableRecipe> {
    private final int rotations;
    private final IBlockState productState;
    private final ItemStack representative;

    public TurntableRecipe(BlockStateIngredient input, IRecipeOutputs recipeOutput, int rotations, IBlockState productState, ItemStack representative) {
        super(input, recipeOutput);
        this.rotations = rotations;
        this.productState = productState;
        this.representative = representative;
    }

    public int getRotations() {
        return rotations;
    }

    public ItemStack getRepresentative() {
        return representative;
    }

    public IBlockState getProductState() {
        return Optional.ofNullable(productState).orElse(Blocks.AIR.getDefaultState());
    }

    @Override
    public boolean craftRecipe(World world, BlockPos pos, Random rand, IBlockState state) {
        TileTurntable turntable = CraftingManagerTurntable.findTurntable(world, pos);
        if (turntable != null && turntable.getPotteryRotation() >= getRotations()) {
            InventoryUtils.ejectStackWithOffset(world, pos, onCraft(world, pos));
            state.getBlock().onBlockHarvested(world, pos, state, FakePlayerHandler.getSword());
            world.setBlockState(pos, getProductState(), world.isRemote ? 11 : 3);
            return true;
        }
        return false;
    }

    @Override
    public boolean isInvalid() {
        return getInput().isSimple() && ArrayUtils.isEmpty(getInput().getMatchingStacks());
    }

}
