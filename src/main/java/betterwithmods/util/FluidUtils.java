package betterwithmods.util;

import betterwithmods.module.hardcore.world.buckets.BucketsUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import java.util.Arrays;
import java.util.Objects;

public class FluidUtils {


    public static Fluid getFluidFromBlock(World world, BlockPos blockPos, EnumFacing side) {
        IFluidHandler handler = BucketsUtils.getBlockFluidHandler(world, blockPos, side);
        if (handler != null)
            return Arrays.stream(handler.getTankProperties()).map(IFluidTankProperties::getContents).filter(Objects::nonNull).map(FluidStack::getFluid).findFirst().orElse(null);
        return null;
    }


}
