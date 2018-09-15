package betterwithmods.module.hardcore.world.village;

import betterwithmods.common.BWMBlocks;
import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TableChanger implements IChanger {
    @Override
    public boolean canChange(World world, BlockPos pos, IBlockState state) {
        if(state.getBlock() == Blocks.WOODEN_PRESSURE_PLATE) {
            IBlockState below = world.getBlockState(pos.down());
            return below.getBlock() instanceof BlockFence;
        }
        return false;
    }

    @Override
    public IBlockState change(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos.down(), BWMBlocks.WOOD_TABLE.getDefaultState());
        return Blocks.AIR.getDefaultState();
    }
}
