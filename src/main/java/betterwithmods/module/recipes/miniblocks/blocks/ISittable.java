package betterwithmods.module.recipes.miniblocks.blocks;

import betterwithmods.common.entity.EntitySitMount;
import betterwithmods.library.utils.HoldingUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface ISittable {

    AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos);

    default void removeEntities(IBlockState state, World world, BlockPos pos) {
        AxisAlignedBB box = getBoundingBox(state, world, pos).grow(1);
        world.getEntitiesWithinAABB(EntitySitMount.class, box).forEach(EntitySitMount::dismountRidingEntity);
    }

    default boolean attemptToSit(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(playerIn.isSneaking())
            return false;
        if (!playerIn.isRiding() && HoldingUtils.areHandsEmpty(playerIn) && worldIn.getEntitiesWithinAABB(EntitySitMount.class, getBoundingBox(state, worldIn, pos).offset(pos)).isEmpty()) {
            EntitySitMount mount = new EntitySitMount(worldIn, getOffset());
            mount.setPosition(pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5);
            worldIn.spawnEntity(mount);
            playerIn.startRiding(mount);
            return true;
        }
        return false;
    }

    double getOffset();

}
