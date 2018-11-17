package betterwithmods.common.blocks.mechanical;

import betterwithmods.api.block.IOverpower;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.tile.TileScrewPump;
import betterwithmods.library.common.block.BlockBase;
import betterwithmods.library.common.block.IBlockActive;
import betterwithmods.library.utils.DirUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

/**
 * @author mrebhan
 */

public class BlockScrewPump extends BlockBase implements IBlockActive, IOverpower {

    public BlockScrewPump() {
        super(Material.WOOD);

        this.setTickRandomly(true);
        this.setHardness(3.5F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(DirUtils.HORIZONTAL, EnumFacing.NORTH).withProperty(ACTIVE, false));
        setSoundType(SoundType.WOOD);
    }

    public static boolean hasWaterToPump(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        EnumFacing direction = state.getValue(DirUtils.HORIZONTAL);
        BlockPos source = DirUtils.movePos(pos, direction);
        IBlockState sourceState = world.getBlockState(source);
        Block block = sourceState.getBlock();
        Material mat = block.getMaterial(state);
        return (block instanceof BlockFluidBase && ((BlockFluidBase) block).getFluid() == FluidRegistry.WATER) || (block instanceof BlockLiquid && mat == Material.WATER);
    }


    @Override
    public int tickRate(World world) {
        return 5;
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, float flX, float flY, float flZ,
                                            int meta, @Nonnull EntityLivingBase entity, EnumHand hand) {
        IBlockState state = super.getStateForPlacement(world, pos, side, flX, flY, flZ, meta, entity, hand);
        return setFacingInBlock(state, entity.getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity,
                                ItemStack stack) {
        EnumFacing facing = DirUtils.convertEntityOrientationToFlatFacing(entity, EnumFacing.NORTH);
        setFacingInBlock(state, facing);
    }

    public IBlockState setFacingInBlock(IBlockState state, EnumFacing facing) {
        return state.withProperty(DirUtils.HORIZONTAL, facing);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean isActive = false;
        if (meta > 7) {
            isActive = true;
            meta -= 8;
        }
        return this.getDefaultState().withProperty(ACTIVE, isActive).withProperty(DirUtils.HORIZONTAL,
                EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(ACTIVE) ? 8 : 0;
        return meta + state.getValue(DirUtils.HORIZONTAL).getIndex();
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DirUtils.HORIZONTAL, ACTIVE);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        withTile(worldIn, pos).ifPresent(TileScrewPump::onChanged);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        withTile(world, pos).ifPresent(TileScrewPump::onChanged);
        if (isActive(state)) {
            if (world.isAirBlock(pos.up()) && hasWaterToPump(world, pos)) {
                world.setBlockState(pos.up(), BWMBlocks.TEMP_LIQUID_SOURCE.getDefaultState());
            }
        }
        world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
    }

    @Override
    public void nextState(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, state.withProperty(ACTIVE, false).cycleProperty(DirUtils.HORIZONTAL));
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
        return new TileScrewPump();
    }

    @Override
    public void overpower(World world, BlockPos pos) {

    }

    public Optional<TileScrewPump> withTile(World world, BlockPos pos) {
        return Optional.ofNullable(getTile(world, pos));
    }

    public TileScrewPump getTile(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileScrewPump)
            return (TileScrewPump) tile;
        return null;
    }

    @Override
    public boolean rotates() {
        return true;
    }
}
