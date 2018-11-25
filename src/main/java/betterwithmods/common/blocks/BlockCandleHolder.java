package betterwithmods.common.blocks;

import betterwithmods.common.BWMOreDictionary;
import betterwithmods.module.general.moreheads.common.BlockHead;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockCandleHolder extends BlockStickBase {

    public BlockCandleHolder() {
        super(Material.IRON);
        setDefaultState(getDefaultState().withProperty(CONNECTION, Connection.DISCONNECTED).withProperty(GROUND, true));
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, @Nonnull BlockPos pos) {
        return worldIn.isSideSolid(pos.down(), EnumFacing.UP) || worldIn.getBlockState(pos.down()).getBlock() instanceof BlockCandleHolder;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!canPlaceBlockAt(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    @Override
    public IBlockState getConnections(IBlockState state, IBlockAccess world, BlockPos pos) {
        IBlockState above = world.getBlockState(pos.up());
        Block block = above.getBlock();
        ItemStack stack = new ItemStack(block, 1, block.getMetaFromState(above));
        BlockFaceShape shape = above.getBlockFaceShape(world, pos.up(), EnumFacing.DOWN);

        if (block == this) {
            return state.withProperty(CONNECTION, Connection.CONNECTED);
        } else if (shape == BlockFaceShape.SOLID || shape == BlockFaceShape.CENTER_SMALL || shape == BlockFaceShape.CENTER || isUprightTorch(above) || BWMOreDictionary.isOre(stack, "blockCandle")) {
            return state.withProperty(CONNECTION, Connection.CANDLE);
        } else if (shape == BlockFaceShape.CENTER_BIG || block instanceof BlockSkull || block instanceof BlockHead) {
            return state.withProperty(CONNECTION, Connection.SKULL);
        }
        return state;
    }

    public boolean isUprightTorch(IBlockState state) {
        return state.getBlock() instanceof BlockTorch && state.getValue(BlockTorch.FACING) == EnumFacing.UP;
    }

    @Override
    public double getHeight(IBlockState state) {
        Connection c = state.getValue(CONNECTION);
        return c == Connection.DISCONNECTED ? 14d / 16d : 1;
    }

    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing) {
        return facing == EnumFacing.DOWN ? BlockFaceShape.CENTER_SMALL : (facing == EnumFacing.UP ? BlockFaceShape.CENTER : BlockFaceShape.UNDEFINED);
    }
}
