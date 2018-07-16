package betterwithmods.module.hardcore.beacons;

import betterwithmods.common.registry.block.recipe.BlockIngredient;
import betterwithmods.common.registry.block.recipe.StateIngredient;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.awt.*;

public abstract class BeaconEffect {

    protected BlockIngredient structureBlock;
    protected Class<? extends EntityLivingBase> validEntityType;
    protected Color baseBeamColor;
    protected int[] effectRanges;

    public BeaconEffect(BlockIngredient structureBlock, Class<? extends EntityLivingBase> validEntityType) {
        this.structureBlock = structureBlock;
        this.validEntityType = validEntityType;
        this.effectRanges = new int[]{20, 40, 60, 80};
    }

    public BeaconEffect setBaseBeamColor(Color baseBeamColor) {
        this.baseBeamColor = baseBeamColor;
        return this;
    }

    public BlockIngredient getStructureBlock() {
        return structureBlock;
    }

    public Class<? extends EntityLivingBase> getValidEntityType() {
        return validEntityType;
    }

    public Color getBaseBeaconBeamColor() {
        return baseBeamColor;
    }

    public boolean isBlockStateValid(World world, BlockPos pos, IBlockState blockState) {
       return structureBlock.apply(world, pos, blockState);
    }

    public NonNullList<EntityLivingBase> getEntitiesInRange(World world, BlockPos pos, int beaconLevel) {
        int radius = effectRanges[Math.min(beaconLevel - 1, 3)];
        AxisAlignedBB box = new AxisAlignedBB(pos, pos.add(1, 1, 1)).grow(radius);
        return InvUtils.asNonnullList(world.getEntitiesWithinAABB(getValidEntityType(), box));
    }

    public abstract void apply(NonNullList<EntityLivingBase> entitiesInRange, @Nonnull World world, @Nonnull BlockPos pos, int beaconLevel);

    public abstract boolean onPlayerInteracted(World world, BlockPos pos, int level, EntityPlayer player, EnumHand hand, ItemStack stack);

    public abstract void onBeaconBreak(World world, BlockPos pos, int level);



}
