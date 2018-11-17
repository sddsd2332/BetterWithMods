package betterwithmods.common.blocks;

import betterwithmods.common.BWMBlocks;
import betterwithmods.library.common.block.BlockBase;
import betterwithmods.library.utils.DirUtils;
import betterwithmods.library.utils.InventoryUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static net.minecraft.util.EnumFacing.Axis.*;


/**
 * Created by primetoxinz on 5/17/17.
 */
public class BlockStake extends BlockBase {


    public static final PropertyBool[] Z_ROTATE = new PropertyBool[]{DirUtils.SOUTH, DirUtils.NORTH, DirUtils.DOWN, DirUtils.UP, DirUtils.EAST, DirUtils.WEST};
    public static final PropertyBool[] X_ROTATE = new PropertyBool[]{DirUtils.SOUTH, DirUtils.NORTH, DirUtils.WEST, DirUtils.EAST, DirUtils.DOWN, DirUtils.UP};

    public BlockStake() {
        super(Material.WOOD);
        setDefaultState(getDefaultState().withProperty(DirUtils.NORTH, false).withProperty(DirUtils.SOUTH, false).withProperty(DirUtils.WEST, false).withProperty(DirUtils.EAST, false).withProperty(DirUtils.UP, false).withProperty(DirUtils.DOWN, false));
    }

    public static boolean getDirection(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        Block block = world.getBlockState(pos.offset(facing)).getBlock();
        return block instanceof BlockStakeString;
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(DirUtils.FACING, facing.getOpposite());
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DirUtils.FACING, DirUtils.UP, DirUtils.DOWN, DirUtils.NORTH, DirUtils.SOUTH, DirUtils.WEST, DirUtils.EAST);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(DirUtils.FACING).getIndex();
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(DirUtils.FACING, EnumFacing.byIndex(meta));
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItemMainhand();
        if (stack.isItemEqual(new ItemStack(Items.STRING)))
            return placeString(worldIn, pos, side, stack);
        return false;
    }

    public boolean placeString(World world, BlockPos pos, EnumFacing facing, ItemStack stack) {
        int count = stack.getCount();
        int build = -1;
        for (int i = 1; i <= 64; i++) {
            IBlockState stake = world.getBlockState(pos.offset(facing, i));
            if (stake.getBlock() instanceof BlockStake) {
                build = i;
                break;
            } else if (!world.isAirBlock(pos.offset(facing, i)) && !stake.getBlock().isReplaceable(world, pos.offset(facing, i)) && stake.getBlock() != BWMBlocks.STAKE_STRING)
                return false;
        }
        if (build > -1 && count >= (build - 1)) {
            int remove = 0;
            //stack.shrink(build - 1);
            for (int i = 1; i < build; i++) {
                if (world.getBlockState(pos.offset(facing, i)).getBlock() != BWMBlocks.STAKE_STRING) {
                    world.setBlockState(pos.offset(facing, i), BWMBlocks.STAKE_STRING.getDefaultState());
                    remove++;
                }
            }
            stack.shrink(remove);
            return true;
        }
        return false;
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(DirUtils.FACING)) {

            default:
                return new AxisAlignedBB(6 / 16f, 0, 6 / 16f, 10f / 16, 12f / 16f, 10f / 16f);
            case UP:
                return new AxisAlignedBB(6 / 16f, 4 / 16f, 6 / 16f, 10f / 16, 1, 10f / 16f);
            case NORTH:
                return new AxisAlignedBB(6 / 16f, 6 / 16f, 12f / 16f, 10f / 16f, 10f / 16, 0);
            case SOUTH:
                return new AxisAlignedBB(6 / 16f, 6 / 16f, 4f / 16f, 10f / 16f, 10f / 16, 1);
            case WEST:
                return new AxisAlignedBB(12f / 16f, 6 / 16f, 6 / 16f, 0, 10f / 16, 10f / 16f);
            case EAST:
                return new AxisAlignedBB(4f / 16f, 6 / 16f, 6 / 16f, 1, 10f / 16, 10f / 16f);

        }
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        IBlockState newState = state;
        EnumFacing facing = state.getValue(DirUtils.FACING);
        EnumFacing.Axis axis = facing.getAxis();

        boolean inverted = facing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE; //so technically not inverted.
        PropertyBool[] transform = DirUtils.DIR_PROP;
        if (axis == Z)
            transform = Z_ROTATE;
        else if (axis == X)
            transform = X_ROTATE;

        for (int i = 0; i < EnumFacing.VALUES.length; i++) {
            EnumFacing front = EnumFacing.byIndex(i);
            if (inverted && ((axis != Y && front.getAxis() != Y) || (axis == Y && front.getAxis() != X))) //Dude idk why but it needs this.
                front = front.getOpposite();
            newState = newState.withProperty(transform[i], getDirection(worldIn, pos, front));
        }
        return newState;
    }

    @Nonnull
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
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

    @Override
    public boolean canPlaceBlockOnSide(@Nonnull World worldIn, @Nonnull BlockPos pos, EnumFacing side) {
        return worldIn.isSideSolid(pos.offset(side.getOpposite()), side);
    }

    @Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        worldIn.notifyNeighborsOfStateChange(pos, this, true);
        for (EnumFacing facing : EnumFacing.VALUES) {
            Block block = worldIn.getBlockState(pos.offset(facing)).getBlock();
            if (block instanceof BlockStakeString) {
                ((BlockStakeString) block).drop(worldIn, pos.offset(facing));
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    public boolean isSupported(World world, BlockPos pos, IBlockState state) {
        EnumFacing side = state.getValue(DirUtils.FACING);
        return world.isSideSolid(pos.offset(side), side.getOpposite());
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!isSupported(worldIn, pos, state)) {
            InventoryUtils.ejectStackWithOffset(worldIn, pos, getItem(worldIn, pos, state));
            worldIn.setBlockToAir(pos);
        }
    }
}
