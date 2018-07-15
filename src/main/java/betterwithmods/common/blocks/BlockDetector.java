package betterwithmods.common.blocks;

import betterwithmods.common.BWMBlocks;
import betterwithmods.util.DirUtils;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockDetector extends BWMBlock {
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockDetector() {
        super(Material.ROCK);
        this.setHardness(3.5F);
        this.setSoundType(SoundType.STONE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(DirUtils.FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));
        this.setTickRandomly(true);
        this.setHarvestLevel("pickaxe", 0);
    }


    @SuppressWarnings("deprecation")
    @Override
    public boolean isSideSolid(IBlockState base_state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    public int tickRate(World world) {
        return 4;
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, float flX, float flY, float flZ, int meta, @Nonnull EntityLivingBase entity, EnumHand hand) {
        IBlockState state = super.getStateForPlacement(world, pos, side, flX, flY, flZ, meta, entity, hand);
        return setFacingInBlock(state, DirUtils.convertEntityOrientationToFacing(entity, side));
    }


    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack, @Nullable EnumFacing face, float hitX, float hitY, float hitZ) {
        super.onBlockPlacedBy(world, pos, state, placer, stack, face, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, entity, stack);
        EnumFacing facing = DirUtils.convertEntityOrientationToFacing(entity, EnumFacing.NORTH);
        setFacingInBlock(state, facing);

    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        setBlockOn(world, pos, false);
        world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        setBlockOn(world, pos, detectBlock(world, pos));
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canProvidePower(IBlockState state) {
        return state.getValue(ACTIVE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing facing) {
        return isBlockOn(world, pos) ? 15 : 0;
    }

    public EnumFacing getFacing(IBlockState state) {
        return state.getValue(DirUtils.FACING);
    }

    public IBlockState setFacingInBlock(IBlockState state, EnumFacing facing) {
        return state.withProperty(DirUtils.FACING, facing);
    }

    public boolean isBlockOn(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getValue(ACTIVE);
    }

    public void setBlockOn(World world, BlockPos pos, boolean on) {
        if (on != isBlockOn(world, pos)) {
            IBlockState state = world.getBlockState(pos).withProperty(ACTIVE, on);

            if (on) {
                world.playSound(null, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.7F);
            }

            world.setBlockState(pos, state);

            for (int i = 0; i < 6; i++) {
                world.neighborChanged(pos.offset(EnumFacing.getFront(i)), this, pos);
            }
        }
        world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
    }

    public boolean detectBlock(World world, BlockPos pos) {
        EnumFacing facing = getFacing(world.getBlockState(pos));
        BlockPos offset = pos.offset(facing);
        IBlockState blockState = world.getBlockState(offset);
        Block target = blockState.getBlock();

        if (facing == EnumFacing.UP && world.isAirBlock(offset) && (world.getBiomeForCoordsBody(offset).canRain() && world.canBlockSeeSky(offset) && (world.isRaining() || world.isThundering()))) {
            return true;
        } else if (target == BWMBlocks.LENS) {
            BlockLens lens = (BlockLens) target;
            return lens.getFacing(blockState) == DirUtils.getOpposite(getFacing(world.getBlockState(pos))) && lens.isLit(world, offset);
        } else if (blockState.getMaterial().isSolid() || target instanceof BlockVine || target instanceof BlockReed) {
            return true;
        } else if (target == BWMBlocks.LIGHT_SOURCE) {
            return blockState.getValue(DirUtils.FACING) == facing;
        } else if (target instanceof BlockHemp) {
            return blockState.getValue(BlockHemp.TOP);
        } else if (world.isAirBlock(offset)) {
            int x = offset.getX();
            int y = offset.getY();
            int z = offset.getZ();
            AxisAlignedBB collisionBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
            List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class, collisionBox, Entity::isEntityAlive);
            world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
            if (!entityList.isEmpty())
                return true;
            else {
                //Crops except hemp;

                BlockPos downOffset = offset.down();
                IBlockState downState = world.getBlockState(downOffset);
                Block downBlock = downState.getBlock();

                if (!(downBlock instanceof BlockHemp) && downBlock instanceof BlockCrops) {
                    return ((BlockCrops) downBlock).isMaxAge(downState);
                } else if (downBlock == Blocks.NETHER_WART) {
                    return downState.getValue(BlockNetherWart.AGE) >= 3;
                }
            }
        }
        return false;
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean isActive = false;
        if (meta > 7) {
            isActive = true;
            meta -= 8;
        }
        return this.getDefaultState().withProperty(ACTIVE, isActive).withProperty(DirUtils.FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(ACTIVE) ? 8 : 0;
        return meta + state.getValue(DirUtils.FACING).getIndex();
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DirUtils.FACING, ACTIVE);
    }

    @Override
    public void nextState(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, state.withProperty(ACTIVE, false).cycleProperty(DirUtils.FACING));
    }

    @Override
    public boolean rotates() {
        return true;
    }
}
