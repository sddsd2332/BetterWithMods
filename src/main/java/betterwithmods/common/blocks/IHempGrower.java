package betterwithmods.common.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IHempGrower {

    boolean canGrowHemp(IBlockAccess world, BlockPos pos, IBlockState state);

}
