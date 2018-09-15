package betterwithmods.module.hardcore.world.structures;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IChanger {

    boolean canChange(World world, BlockPos pos, BlockPos relativePos, IBlockState original);

    IBlockState change(World world, BlockPos pos, BlockPos relativePos, IBlockState original);

}
