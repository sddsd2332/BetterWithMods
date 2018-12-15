package betterwithmods.common.blocks;

import betterwithmods.common.blocks.camo.BlockDynamic;
import betterwithmods.common.tile.TileDynamic;
import betterwithmods.lib.ModLib;
import betterwithmods.library.common.block.BlockBase;
import betterwithmods.library.common.block.IBlockActive;
import betterwithmods.library.common.block.creation.BlockEntryBuilderGenerator;
import betterwithmods.module.internal.SoundRegistry;
import betterwithmods.module.recipes.miniblocks.ISubtypeProvider;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.security.Provider;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;

public class BlockChime extends BlockDynamic<TileDynamic> implements IBlockActive {
    public static final Set<Block> BLOCKS = Sets.newHashSet();
    private static final AxisAlignedBB CHIME_AABB = new AxisAlignedBB(0.3125D, 0.375D, 0.3125D, 0.6875D, 0.875D, 0.6875D);
    private final Supplier<SoundEvent> chimeSound;

    public BlockChime(Supplier<SoundEvent>  chimeSound, Material material, ISubtypeProvider subtypes) {
        super(material, subtypes);
        this.chimeSound = chimeSound;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return true;
        else {
            if (!isActive(state)) {
                setActive(world,pos, true);
                world.playSound(null, pos, chimeSound.get(), SoundCategory.BLOCKS, 0.4F, 1.0F);
                for (EnumFacing facing : EnumFacing.VALUES)
                    world.notifyNeighborsOfStateChange(pos.offset(facing), this, false);
            }
            return true;
        }
    }

    @Override
    public int tickRate(World world) {
        return 20;
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

    @Override
    public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {
        return world.getBlockState(pos.up()).isSideSolid(world, pos.up(), EnumFacing.DOWN) || world.getBlockState(pos.up()).getBlock() instanceof BlockFence || world.getBlockState(pos.up()).getBlock() instanceof net.minecraft.block.BlockPane || world.getBlockState(pos.up()).getBlock() instanceof BlockRope;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if (isActive(state)) {
            for (EnumFacing facing : EnumFacing.VALUES)
                world.notifyNeighborsOfStateChange(pos.offset(facing), this, false);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        if (!canPlaceBlockAt(world, pos)) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        } else {
            world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
        }
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CHIME_AABB;
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        boolean storm = detectStorming(world, pos) || isEntityColliding(world, pos);
        boolean isActive = isActive(state);

        if (storm != isActive) {
            setActive(world,pos, storm);
            world.notifyNeighborsOfStateChange(pos, this, false);
            for (EnumFacing facing : EnumFacing.VALUES)
                world.notifyNeighborsOfStateChange(pos.offset(facing), this, false);
        }
        if (storm)
            world.playSound(null, pos, chimeSound.get(), SoundCategory.BLOCKS, 0.25F + (rand.nextFloat() - rand.nextFloat() * 0.1F), 1.0F);
        world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
    }

    private boolean detectStorming(World world, BlockPos pos) {
        return (world.isRaining() || world.isThundering()) && isNearOpenAir(world, pos);
    }

    private boolean isNearOpenAir(World world, BlockPos pos) {
        for (int x = -5; x < 6; x++) {
            for (int y = -2; y < 4; y++) {
                for (int z = -5; z < 6; z++) {
                    BlockPos check = pos.add(x, y, z);
                    if (world.canBlockSeeSky(check))
                        return true;
                }
            }
        }
        return false;
    }

    private boolean isEntityColliding(World world, BlockPos pos) {
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.getX() + 0.3125D, pos.getY() + 0.375D, pos.getZ() + 0.3125D, pos.getX() + 0.6875D, pos.getY() + 1.0D, pos.getZ() + 0.6875D));
        return !entities.isEmpty();
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (!isActive(state)) {
            setActive(world,pos, true);
            world.notifyNeighborsOfStateChange(pos, this, false);
            for (EnumFacing facing : EnumFacing.VALUES)
                world.notifyNeighborsOfStateChange(pos.offset(facing), this, false);
            world.playSound(null, pos, chimeSound.get(), SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return getWeakPower(state, world, pos, side);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing) {
        if(isActive(state))
            return 15;
        return 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ACTIVE) ? 1 : 0;
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ACTIVE, meta == 1);
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public IProperty<?>[] getProperties() {
        return IBlockActive.super.getProperties();
    }

}
