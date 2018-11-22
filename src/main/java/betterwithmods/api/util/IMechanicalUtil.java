package betterwithmods.api.util;

import betterwithmods.api.tile.IAxle;
import betterwithmods.api.tile.IMechanicalPower;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IMechanicalUtil {

    IMechanicalPower getMechanicalPower(IBlockAccess world, BlockPos pos, EnumFacing facing);

    IAxle getAxle(IBlockAccess world, BlockPos pos, EnumFacing facing);

    boolean isRedstonePowered(IBlockAccess world, BlockPos pos);

    boolean canInput(IBlockAccess world, BlockPos pos, EnumFacing facing);

    boolean isAxle(IBlockAccess world, BlockPos pos, EnumFacing facing);

    int getPowerOutput(IBlockAccess world, BlockPos pos, EnumFacing facing);
}
