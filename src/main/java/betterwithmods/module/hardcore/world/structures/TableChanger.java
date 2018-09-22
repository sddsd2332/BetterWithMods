package betterwithmods.module.hardcore.world.structures;

import betterwithmods.module.recipes.miniblocks.MiniBlocks;
import betterwithmods.module.recipes.miniblocks.MiniType;
import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;

public class TableChanger implements IChanger {
    @Override
    public boolean canChange(StructureComponent structure, World world, BlockPos pos, BlockPos relativePos, IBlockState state) {
        if (state.getBlock() == Blocks.WOODEN_PRESSURE_PLATE) {
            IBlockState below = world.getBlockState(pos.down());
            return below.getBlock() instanceof BlockFence;
        }
        return false;
    }

    @Override
    public IBlockState change(StructureComponent structure, World world, BlockPos pos, BlockPos relativePos, IBlockState state) {
        MiniBlocks.placeMini(world, pos.down(), MiniType.TABLE, Blocks.PLANKS.getDefaultState());
        return Blocks.AIR.getDefaultState();
    }
}
