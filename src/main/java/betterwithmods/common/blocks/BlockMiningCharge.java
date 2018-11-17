package betterwithmods.common.blocks;

import betterwithmods.common.entity.EntityMiningCharge;
import betterwithmods.library.utils.DirUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by primetoxinz on 9/5/16.
 */

public class BlockMiningCharge extends BlockTNT {
    private static final AxisAlignedBB D_AABB = new AxisAlignedBB(0, .5, 0, 1, 1, 1);
    private static final AxisAlignedBB U_AABB = new AxisAlignedBB(0, 0, 0, 1, .5, 1);
    private static final AxisAlignedBB N_AABB = new AxisAlignedBB(0, 0, .5, 1, 1, 1);
    private static final AxisAlignedBB S_AABB = new AxisAlignedBB(0, 0, 0, 1, 1, .5);
    private static final AxisAlignedBB W_AABB = new AxisAlignedBB(.5, 0, 0, 1, 1, 1);
    private static final AxisAlignedBB E_AABB = new AxisAlignedBB(0, 0, 0, .5, 1, 1);

    public BlockMiningCharge() {
        setSoundType(SoundType.PLANT);
    }

    @Nonnull
    public static ItemStack dispenser(@Nonnull IBlockSource source, @Nonnull ItemStack stack) {
        World worldIn = source.getWorld();
        EnumFacing facing = source.getBlockState().getValue(BlockDispenser.FACING);
        BlockPos pos = source.getBlockPos().offset(facing);
        EntityMiningCharge miningCharge = new EntityMiningCharge(worldIn, pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, null, facing);
        miningCharge.setNoGravity(false);
        worldIn.spawnEntity(miningCharge);
        worldIn.playSound(null, miningCharge.posX, miningCharge.posY, miningCharge.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
        return stack;
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

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing facing = state.getValue(DirUtils.FACING);
        switch (facing) {
            case DOWN://DOWN
                return D_AABB;
            case UP://UP
                return U_AABB;
            case NORTH:
                return N_AABB;
            case SOUTH:
                return S_AABB;
            case WEST:
                return W_AABB;
            case EAST:
                return E_AABB;
            default:
                return FULL_BLOCK_AABB;
        }

    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, EXPLODE, DirUtils.FACING);
    }

    @Override
    public void explode(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nullable EntityLivingBase igniter) {
        if (!worldIn.isRemote && state.getValue(EXPLODE)) {
            EntityMiningCharge miningCharge = new EntityMiningCharge(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), igniter, getFacing(state));
            worldIn.spawnEntity(miningCharge);
            worldIn.playSound(null, miningCharge.posX, miningCharge.posY, miningCharge.posZ, SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    public EnumFacing getFacing(IBlockState state) {
        return state.getValue(DirUtils.FACING);
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(DirUtils.FACING, facing);
    }

    @Override
    public boolean canPlaceBlockOnSide(@Nonnull World worldIn, @Nonnull BlockPos pos, EnumFacing side) {
        return worldIn.isSideSolid(pos.offset(side.getOpposite()), side);
    }

    @Override
    public void onBlockAdded(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
        if (worldIn.isBlockPowered(pos)) {
            this.onPlayerDestroy(worldIn, pos, state.withProperty(EXPLODE, true));
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void neighborChanged(@Nonnull IBlockState state, World worldIn, @Nonnull BlockPos pos, Block blockIn, BlockPos other) {
        if (worldIn.isBlockPowered(pos)) {
            this.onPlayerDestroy(worldIn, pos, state.withProperty(EXPLODE, true));
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void onBlockExploded(World world, @Nonnull BlockPos pos, @Nonnull Explosion explosion) {
        onBlockExploded(world, pos, explosion);
        world.setBlockToAir(pos);
    }

    @Override
    public void onExplosionDestroy(World worldIn, @Nonnull BlockPos pos, @Nonnull Explosion explosionIn) {
        if (!worldIn.isRemote) {
            EntityMiningCharge miningCharge = new EntityMiningCharge(worldIn, (double) ((float) pos.getX() + 0.5F), (double) pos.getY(), (double) ((float) pos.getZ() + 0.5F), explosionIn.getExplosivePlacedBy(), getFacing(worldIn.getBlockState(pos)));
            miningCharge.setFuse((short) (worldIn.rand.nextInt(miningCharge.getFuse() / 4) + miningCharge.getFuse() / 8));
            worldIn.spawnEntity(miningCharge);
        }
    }

    @Override
    public void onPlayerDestroy(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        this.explode(worldIn, pos, state, null);
    }

    @Override
    public void onEntityCollision(World worldIn, @Nonnull BlockPos pos, IBlockState state, Entity entityIn) {
        if (!worldIn.isRemote && entityIn instanceof EntityArrow) {
            EntityArrow entityarrow = (EntityArrow) entityIn;
            if (entityarrow.isBurning()) {
                EntityLivingBase shooter = entityarrow.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase) entityarrow.shootingEntity : null;
                this.explode(worldIn, pos, worldIn.getBlockState(pos).withProperty(EXPLODE, true), shooter);
                worldIn.setBlockToAir(pos);
            }
        }
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean explode = (meta & 1) > 0;
        EnumFacing facing = EnumFacing.byIndex(meta >> 1);
        return this.getDefaultState().withProperty(EXPLODE, explode).withProperty(DirUtils.FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int facing = state.getValue(DirUtils.FACING).getIndex() << 1;
        int explode = state.getValue(EXPLODE) ? 1 : 0;
        return explode | facing;
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face != getFacing(state).getOpposite() ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
    }


}