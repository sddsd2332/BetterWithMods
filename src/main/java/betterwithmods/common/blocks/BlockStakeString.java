package betterwithmods.common.blocks;

import betterwithmods.library.common.block.BlockBase;
import betterwithmods.library.utils.DirUtils;
import betterwithmods.library.utils.InventoryUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class BlockStakeString extends BlockBase {
    public BlockStakeString() {
        super(Material.CLOTH);
        setDefaultState(getDefaultState().withProperty(DirUtils.NORTH, false).withProperty(DirUtils.SOUTH, false).withProperty(DirUtils.WEST, false).withProperty(DirUtils.EAST, false).withProperty(DirUtils.UP, false).withProperty(DirUtils.DOWN, false));
    }

    public static boolean getDirection(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        Block block = world.getBlockState(pos.offset(facing)).getBlock();
        return block instanceof BlockStakeString || block instanceof BlockStake;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB entityBox, @Nonnull List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, p_185477_7_);
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        state = getActualState(state, source, pos);
        AxisAlignedBB box = new AxisAlignedBB(7 / 16d, 7 / 16d, 7 / 16d, 9 / 16d, 9 / 16d, 9 / 16d);
        if (state.getValue(DirUtils.EAST))
            box = box.union(new AxisAlignedBB(0, 7 / 16d, 7 / 16d, 0.5, 9 / 16d, 9 / 16d));
        if (state.getValue(DirUtils.WEST))
            box = box.union(new AxisAlignedBB(0.5, 7 / 16d, 7 / 16d, 1, 9 / 16d, 9 / 16d));
        if (state.getValue(DirUtils.NORTH))
            box = box.union(new AxisAlignedBB(7 / 16d, 7 / 16d, 0, 9 / 16d, 9 / 16d, 0.5));
        if (state.getValue(DirUtils.SOUTH))
            box = box.union(new AxisAlignedBB(7 / 16d, 7 / 16d, 0.5, 9 / 16d, 9 / 16d, 1));
        return box;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public IProperty<?>[] getProperties() {
        return new IProperty[]{DirUtils.UP, DirUtils.DOWN, DirUtils.NORTH, DirUtils.SOUTH, DirUtils.WEST, DirUtils.EAST};
    }

    @Nonnull
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, @Nonnull IBlockState state) {
        return new ItemStack(Items.STRING);
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
        drops.add(getItem((World) world, pos, state));
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        IBlockState newState = state;

        for (int i = 0; i < EnumFacing.VALUES.length; i++) {
            newState = newState.withProperty(DirUtils.DIR_PROP[i], getDirection(worldIn, pos, EnumFacing.byIndex(i)));
        }
        return newState;
    }

    @Nonnull
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    public void drop(World world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() instanceof BlockStakeString) {
            InventoryUtils.ejectStackWithOffset(world, pos, getItem(world, pos, world.getBlockState(pos)));
            world.setBlockToAir(pos);
        }
    }

    @Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        state = state.getActualState(worldIn, pos);
        for (int i = 0; i < DirUtils.DIR_PROP.length; i++) {
            if (state.getValue(DirUtils.DIR_PROP[i])) {
                drop(worldIn, pos.offset(EnumFacing.byIndex(i)));
            }
        }
        super.breakBlock(worldIn, pos, state);
    }


    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        return false;
    }

}
