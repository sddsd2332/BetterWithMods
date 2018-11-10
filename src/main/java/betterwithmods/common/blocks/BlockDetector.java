package betterwithmods.common.blocks;

import betterwithmods.library.common.block.BlockActiveFacing;
import betterwithmods.library.utils.DirUtils;
import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

public class BlockDetector extends BlockActiveFacing {
    public static Set<IDetection> DETECTION_HANDLERS = Sets.newHashSet();

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
        return 1;
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, float flX, float flY, float flZ, int meta, @Nonnull EntityLivingBase entity, EnumHand hand) {
        return withFacing(super.getStateForPlacement(world, pos, side, flX, flY, flZ, meta, entity, hand), EnumFacing.getDirectionFromEntityLiving(pos, entity));
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
                world.neighborChanged(pos.offset(EnumFacing.byIndex(i)), this, pos);
            }
        }
        world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
    }

    public boolean detectBlock(World world, BlockPos pos) {
        EnumFacing facing = getFacing(world.getBlockState(pos));
        BlockPos offset = pos.offset(facing);
        for (IDetection detection : DETECTION_HANDLERS) {
            if (detection.apply(facing, world, pos, offset))
                return true;
        }
        return false;
    }


    @Override
    public void nextState(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, state.withProperty(ACTIVE, false).cycleProperty(DirUtils.FACING));
    }

    @Override
    public boolean rotates() {
        return true;
    }

    public interface IDetection {
        boolean apply(EnumFacing direction, World world, BlockPos pos, BlockPos offset);
    }

    public static class EntityDetection implements IDetection {

        @Override
        public boolean apply(EnumFacing direction, World world, BlockPos pos, BlockPos offset) {
            int x = offset.getX();
            int y = offset.getY();
            int z = offset.getZ();
            AxisAlignedBB collisionBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
            return world.getEntitiesWithinAABB(Entity.class, collisionBox, Entity::isEntityAlive).stream().findAny().isPresent();
        }
    }

    public static class IngredientDetection implements IDetection {
        private BlockStateIngredient ingredients;
        private Predicate<EnumFacing> direction;

        public IngredientDetection(BlockStateIngredient ingredients) {
            this(ingredients, facing -> true);
        }


        public IngredientDetection(BlockStateIngredient ingredients, Predicate<EnumFacing> direction) {
            this.ingredients = ingredients;
            this.direction = direction;
        }

        public boolean apply(EnumFacing direction, World world, BlockPos pos, BlockPos offset) {
            if (this.direction.test(direction)) {
                return ingredients.test(world, offset, world.getBlockState(offset));
            }
            return false;
        }
    }

}
