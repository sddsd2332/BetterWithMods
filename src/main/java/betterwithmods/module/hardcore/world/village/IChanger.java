package betterwithmods.module.hardcore.world.village;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IChanger {

    boolean canChange(World world, BlockPos pos, IBlockState original);

    IBlockState change(World world, BlockPos pos, IBlockState original);

}
