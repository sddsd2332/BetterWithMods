package betterwithmods.common.blocks.mechanical;

import betterwithmods.api.block.IOverpower;
import betterwithmods.common.BWMSounds;
import betterwithmods.common.blocks.BWMBlock;
import betterwithmods.common.tile.TileAxle;
import betterwithmods.util.DirUtils;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

import static betterwithmods.util.DirUtils.AXIS;
import static net.minecraft.util.EnumFacing.Axis.Y;

public class BlockAxle extends BWMBlock implements IOverpower, IBlockActive {

    private static final AxisAlignedBB X_AABB = new AxisAlignedBB(0.0F, 0.375F, 0.375F, 1.0F, 0.625F, 0.625F);
    private static final AxisAlignedBB Y_AABB = new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
    private static final AxisAlignedBB Z_AABB = new AxisAlignedBB(0.375F, 0.375F, 0.0F, 0.625F, 0.625F, 1.0F);
    private final int minPower;
    private final int maxPower;
    private final int maxSignal;

    public BlockAxle(Material material, int minPower, int maxPower, int maxSignal) {
        super(material);
        this.minPower = minPower;
        this.maxPower = maxPower;
        this.maxSignal = maxSignal;
        this.setDefaultState(this.blockState.getBaseState().withProperty(AXIS, Y).withProperty(ACTIVE, false));
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS, ACTIVE);
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(AXIS, facing.getAxis());
    }


    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(AXIS, DirUtils.getAxis(meta >> 2)).withProperty(ACTIVE, (meta & 1) == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int axis = state.getValue(AXIS).ordinal() << 2;
        int active = state.getValue(ACTIVE) ? 1 : 0;
        return active | axis;
    }


    @Override
    public void nextState(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, state.withProperty(ACTIVE, false).cycleProperty(AXIS));
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        return false;
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

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(AXIS)) {
            case X:
                return X_AABB;
            case Y:
                return Y_AABB;
            case Z:
            default:
                return Z_AABB;
        }
    }


    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleBlockUpdate(pos, this, 1, 5);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        onChange(world, pos);
    }


    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        onChange(worldIn, pos);
    }

    public void onChange(World world, BlockPos pos) {
        if (!world.isRemote) {
            withTile(world, pos).ifPresent(TileAxle::onChanged);
        }
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileAxle(maxPower, minPower, (byte) (maxSignal + 1));
    }

    public Optional<TileAxle> withTile(World world, BlockPos pos) {
        return Optional.ofNullable(getTile(world, pos));
    }

    public TileAxle getTile(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileAxle)
            return (TileAxle) tile;
        return null;
    }

    public EnumFacing[] getAxisDirections(IBlockState state) {
        if (state.getBlock() instanceof BlockAxle)
            return DirUtils.getAxisDirection(state.getValue(AXIS));
        return new EnumFacing[0];
    }

    public EnumFacing.Axis getAxis(IBlockState state) {
        return state.getValue(AXIS);
    }

    @Override
    public void overpower(World world, BlockPos pos) {
        if (doesOverpower()) {
            world.setBlockToAir(pos);
            InvUtils.ejectStackWithOffset(world, pos, new ItemStack(this, 1, damageDropped(world.getBlockState(pos))));
        }
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (isActive(state)) {
            emitAxleParticles(world, pos, rand);
            if (rand.nextInt(200) == 0)
                world.playSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, BWMSounds.WOODCREAK, SoundCategory.BLOCKS, 0.15F, rand.nextFloat() * 0.1F + 0.5F, false);
        }
    }

    private void emitAxleParticles(World world, BlockPos pos, Random rand) {
        int pow;
        if (FMLClientHandler.instance().hasOptifine()) {
            IBlockState state = getActualState(world.getBlockState(pos), world, pos);
            pow = state.getValue(ACTIVE) ? 3 : 0;
        } else {
            pow = withTile(world, pos).map(TileAxle::getSignal).orElse((byte) 0);
        }
        for (int i = 0; i < pow; i++) {
            float flX = pos.getX() + rand.nextFloat();
            float flY = pos.getY() + rand.nextFloat() * 0.5F + 0.625F;
            float flZ = pos.getZ() + rand.nextFloat();
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, flX, flY, flZ, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public boolean rotates() {
        return true;
    }
}
