package betterwithmods.common.tile;

import betterwithmods.common.fluid.FluidTankRestricted;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nonnull;

public class TileBucket extends TileFluid implements ITickable {
    private int ticks;

    @Override
    public FluidTank createTank() {
        return new FluidTankRestricted(new FluidStack(FluidRegistry.WATER, 0), getCapacity());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    public int getCapacity() {
        return Fluid.BUCKET_VOLUME * 8;
    }

    @Override
    public boolean hasFluid(EnumFacing facing) {
        return true;
    }


    private boolean isWater(IBlockState state) {
        return state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.FLOWING_WATER;
    }

    @Override
    public void update() {
        if (!isFull() && ticks > 100) {
            for (EnumFacing face : EnumFacing.HORIZONTALS) {
                if (isWater(world.getBlockState(pos.offset(face)))) {
                    if (fill(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME), true)) {
                        break;
                    }
                }
            }
            ticks = 0;
        }
        ticks++;
    }
}
