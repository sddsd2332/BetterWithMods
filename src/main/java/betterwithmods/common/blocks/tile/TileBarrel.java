package betterwithmods.common.blocks.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nonnull;

public class TileBarrel extends TileFluid {
    private static final int CAPACITY = Fluid.BUCKET_VOLUME * 8;

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    @Override
    public FluidTank createTank() {
        return new FluidTank(getCapacity());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public boolean hasFluid(EnumFacing facing) {
        return true;
    }

}
