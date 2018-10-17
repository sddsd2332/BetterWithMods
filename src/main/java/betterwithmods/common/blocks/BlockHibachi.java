package betterwithmods.common.blocks;

import betterwithmods.common.BWMBlocks;
import betterwithmods.library.common.block.BlockBase;
import betterwithmods.library.common.block.IBlockActive;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockHibachi extends BlockBase implements IBlockActive, IFireSource {

    public BlockHibachi() {
        super(Material.ROCK);
        this.setTickRandomly(true);
        this.setHardness(3.5F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(ACTIVE, false));
        this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    public int tickRate(World world) {
        return 4;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        boolean powered = world.getRedstonePowerFromNeighbors(pos) > 0;
        Block above = world.getBlockState(pos.up()).getBlock();
        if (powered) {
            if (!isLit(world, pos))
                ignite(world, pos);
            else {
                if (isFire(above)) {
                    if (shouldIgnite(world, pos.up())) {
                        world.playSound(null, pos, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.4F + 0.8F);
                        world.setBlockState(pos.up(), Blocks.FIRE.getDefaultState());
                    }
                }
            }
        } else if (isLit(world, pos))
            extinguish(world, pos);
        else {
            if (isFire(above))
                world.setBlockToAir(pos.up());
        }
    }

    private boolean isFire(World world, BlockPos pos) {
        return isFire(world.getBlockState(pos).getBlock());
    }

    private boolean isFire(Block block) {
        return block == Blocks.FIRE || block == BWMBlocks.STOKED_FLAME;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        if (!isCurrentlyValid(world, pos))
            world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
    }

    @Override
    public boolean isFireSource(@Nonnull World world, BlockPos pos, EnumFacing side) {
        return world.getBlockState(pos).getValue(ACTIVE) && side == EnumFacing.UP;
    }

    public boolean isCurrentlyValid(World world, BlockPos pos) {
        boolean powered = world.getRedstonePowerFromNeighbors(pos) > 0;
        if (isLit(world, pos) != powered)
            return false;
        if (isLit(world, pos)) {
            Block block = world.getBlockState(pos).getBlock();

            if (block != Blocks.FIRE && block != BWMBlocks.STOKED_FLAME) {
                return !shouldIgnite(world, pos.up());
            }
        }
        return true;
    }

    public boolean isLit(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getValue(ACTIVE);
    }

    private boolean shouldIgnite(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().isReplaceable(world, pos);
    }

    private void ignite(World world, BlockPos pos) {
        setLit(world, pos);
        world.playSound(null, pos, SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.4F + 1.0F);

        if (shouldIgnite(world, pos.up()))
            world.setBlockState(pos.up(), Blocks.FIRE.getDefaultState());
    }

    private void extinguish(World world, BlockPos pos) {
        clearLit(world, pos);

        world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

        if (isFire(world, pos)) {
            world.setBlockToAir(pos.up());
        }
    }

    private void setLit(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.withProperty(ACTIVE, true));
    }

    private void clearLit(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.withProperty(ACTIVE, false));
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean isLit = meta == 1;
        return this.getDefaultState().withProperty(ACTIVE, isLit);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ACTIVE) ? 1 : 0;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVE);
    }

    @Override
    public boolean isSource(IBlockAccess world, BlockPos pos, IBlockState state) {
        return true;
    }

    @Override
    public boolean isLit(IBlockAccess world, BlockPos pos, IBlockState state) {
        return isActive(state);
    }
}
