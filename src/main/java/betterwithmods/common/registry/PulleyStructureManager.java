package betterwithmods.common.registry;

import betterwithmods.common.BWMBlocks;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import betterwithmods.library.utils.ingredient.collections.BlockStateIngredientSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by primetoxinz on 6/6/17.
 */
public class PulleyStructureManager {

    public static final BlockStateIngredientSet PULLEY_BLOCKS = new BlockStateIngredientSet();

    static {
        registerPulleyBlock(BWMBlocks.PLATFORM.getDefaultState());
        registerPulleyBlock(BWMBlocks.IRON_WALL.getDefaultState());
    }

    public static void registerPulleyBlock(IBlockState state) {
        registerPulleyBlock(new BlockStateIngredient(state));
    }

    public static void registerPulleyBlock(BlockStateIngredient ingredient) {
        PULLEY_BLOCKS.add(ingredient);
    }

    public static boolean isPulleyBlock(World world, BlockPos pos, IBlockState state) {
        return PULLEY_BLOCKS.stream().anyMatch(s -> s.test(world, pos, state));
    }

}
