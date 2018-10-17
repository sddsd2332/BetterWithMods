package betterwithmods.common.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IFireSource {

    boolean isSource(IBlockAccess world, BlockPos pos, IBlockState state);

    boolean isLit(IBlockAccess world, BlockPos pos, IBlockState state);
}
