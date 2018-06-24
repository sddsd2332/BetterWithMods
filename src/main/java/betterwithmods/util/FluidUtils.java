package betterwithmods.util;

import betterwithmods.util.fluid.BlockLiquidWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FluidUtils {

    @Nullable
    public static IFluidHandler getBlockFluidHandler(World world, BlockPos blockPos, EnumFacing side) {
        IBlockState state = world.getBlockState(blockPos);
        Block block = state.getBlock();
        if (block instanceof IFluidBlock) {
            return new FluidBlockWrapper((IFluidBlock) block, world, blockPos);
        } else if (block instanceof BlockLiquid) {
            return new BlockLiquidWrapper((BlockLiquid) block, world, blockPos);
        }

        //this is bad hack since Item.rayTrace doesn't give not full liquids
        blockPos = blockPos.offset(side);
        state = world.getBlockState(blockPos);
        block = state.getBlock();
        if (block instanceof IFluidBlock) {
            return new FluidBlockWrapper((IFluidBlock) block, world, blockPos);
        } else if (block instanceof BlockLiquid) {
            return new BlockLiquidWrapper((BlockLiquid) block, world, blockPos);
        }

        return null;
    }


    @Nonnull
    public static FluidActionResult tryPickUpFluid(@Nonnull ItemStack emptyContainer, @Nullable EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side) {
        if (emptyContainer.isEmpty() || worldIn == null || pos == null) {
            return FluidActionResult.FAILURE;
        }

        IFluidHandler targetFluidHandler = getBlockFluidHandler(worldIn, pos, side);
        if (targetFluidHandler != null) {
            return FluidUtil.tryFillContainer(emptyContainer, targetFluidHandler, Integer.MAX_VALUE, playerIn, true);
        }
        return FluidActionResult.FAILURE;
    }
}
