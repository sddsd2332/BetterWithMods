package betterwithmods.common.blocks.mechanical;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BWMBlock;
import betterwithmods.util.DirUtils;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static betterwithmods.util.DirUtils.AXIS;
import static net.minecraft.util.EnumFacing.Axis.Y;

`ft.block.material.Material;

public abstract class BlockAxleGenerator extends BWMBlock implements IBlockActive {

    private static final AxisAlignedBB X_AABB = new AxisAlignedBB(0.0F, 0.375F, 0.375F, 1.0F, 0.625F, 0.625F);
    private static final AxisAlignedBB Y_AABB = new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
    private static final AxisAlignedBB Z_AABB = new AxisAlignedBB(0.375F, 0.375F, 0.0F, 0.625F, 0.625F, 1.0F);
    private static final AxisAlignedBB[] BOXES = new AxisAlignedBB[]{X_AABB, Y_AABB,Z_AABB};

    public BlockAxleGenerator(Material material) {
        super(material);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, EnumFacing.Axis.Z).withProperty(ACTIVE, false));
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS, ACTIVE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int axis = state.getValue(AXIS).ordinal();
        int active = state.getValue(ACTIVE) ? 1 : 0;
        return axis | active << 2;
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(AXIS, DirUtils.getAxis(meta & 3)).withProperty(ACTIVE, meta >> 2 == 1);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOXES[state.getValue(AXIS).ordinal()];
    }

    @Override
    public int tickRate(World world) {
        return 20;
    }

    public ItemStack getAxle(IBlockAccess world, BlockPos pos, IBlockState state) {
        return new ItemStack(BWMBlocks.WOODEN_AXLE);
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);
        drops.add(getAxle(world, pos, state));
        drops.add(getItem((World) world, pos, state));

    }

    public IBlockState getAxisState(EnumFacing.Axis axis) {
        return this.getDefaultState().withProperty(AXIS, axis);
    }

    public EnumFacing.Axis getAxis(IBlockState state) {
        return state.getValue(AXIS);
    }

    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return getAxis(state) == Y;
    }

    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face.getAxis() == getAxis(state) ? BlockFaceShape.CENTER : BlockFaceShape.UNDEFINED;
    }
}

