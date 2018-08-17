package betterwithmods.common.blocks;

import betterwithmods.common.BWMBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockSteel extends BWMBlock {

    public BlockSteel() {
        super(Material.IRON);
        setSoundType(SoundType.METAL);
        setHardness(100F);
        setResistance(4000F);
    }

    @Deprecated
    public static IBlockState getBlock(int height) {
        return BWMBlocks.STEEL_BLOCK.getDefaultState();
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return entity instanceof EntityPlayer;
    }

    @Override
    public void onBlockExploded(World world, @Nonnull BlockPos pos, @Nonnull Explosion explosion) {

    }

    @Override
    public int getHarvestLevel(@Nonnull IBlockState state) {
        return 4;
    }

    //Cannot be pushed by a piston
    @Nonnull
    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
        return EnumPushReaction.IGNORE;
    }
}
