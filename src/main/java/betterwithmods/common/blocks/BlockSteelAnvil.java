package betterwithmods.common.blocks;

import betterwithmods.BWMod;
import betterwithmods.common.tile.TileSteelAnvil;
import betterwithmods.util.DirUtils;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by blueyu2 on 11/21/16.
 */
public class BlockSteelAnvil extends BWMBlock {

    public BlockSteelAnvil() {
        super(Material.IRON);
        this.setHardness(5.0F);
        this.setResistance(2000.0F);
        this.setHarvestLevel("pickaxe", 1);
        this.setSoundType(SoundType.ANVIL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(DirUtils.HORIZONTAL, EnumFacing.NORTH));
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
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand) {
        IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
        if (facing.ordinal() < 2)
            facing = DirUtils.convertEntityOrientationToFlatFacing(placer, facing);
        return state.withProperty(DirUtils.HORIZONTAL, facing.rotateY());
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        switch (state.getValue(DirUtils.HORIZONTAL)) {
            case NORTH:
            case SOUTH:
                return new AxisAlignedBB(0.25F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
            case EAST:
            case WEST:
                return new AxisAlignedBB(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 0.75F);
            default:
                return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(DirUtils.HORIZONTAL).getHorizontalIndex();
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(DirUtils.HORIZONTAL, EnumFacing.byHorizontalIndex(meta));
    }

    @Nonnull
    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DirUtils.HORIZONTAL);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
            return true;
        else
            playerIn.openGui(BWMod.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileSteelAnvil();
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face.getAxis() == EnumFacing.Axis.Y ? BlockFaceShape.CENTER_BIG : BlockFaceShape.UNDEFINED;
    }
}
