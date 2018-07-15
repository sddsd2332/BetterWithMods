package betterwithmods.common.blocks;

import betterwithmods.api.tile.IRopeConnector;
import betterwithmods.common.tile.TileBucket;
import betterwithmods.common.tile.TilePulley;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockBucket extends BWMBlock implements IRopeConnector {

    public static final PropertyBool CONNECTED = PropertyBool.create("connected");
    public static final PropertyBool IN_WATER = PropertyBool.create("in_water");
    public static final PropertyBool HAS_WATER = PropertyBool.create("has_water");

    public BlockBucket() {
        super(Material.IRON);
        setDefaultState(getDefaultState().withProperty(CONNECTED, true).withProperty(IN_WATER, false).withProperty(HAS_WATER, false));
        setHardness(5.0F);
        setResistance(10.0F);
        setSoundType(SoundType.METAL);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, IN_WATER, HAS_WATER, CONNECTED);
    }

    @Override
    public EnumFacing getFacing(IBlockState state) {
        return EnumFacing.UP;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return FluidUtil.interactWithFluidHandler(playerIn, hand, worldIn, pos, facing);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileBucket();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState();
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        IBlockState above = worldIn.getBlockState(pos.up());
        if (above.getBlock() instanceof BlockRope || worldIn.getTileEntity(pos.up()) instanceof TilePulley) {
            return getDefaultState().withProperty(CONNECTED, true);
        }
        return super.getActualState(state, worldIn, pos).withProperty(CONNECTED, false);
    }

    @Override
    public boolean canMovePlatforms(World world, BlockPos pos) {
        return false;
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
