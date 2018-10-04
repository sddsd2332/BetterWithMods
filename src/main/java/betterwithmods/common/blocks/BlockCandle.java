package betterwithmods.common.blocks;

import betterwithmods.lib.ModLib;
import betterwithmods.library.common.block.creation.ColoredGenerator;
import betterwithmods.library.utils.colors.ColorUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static net.minecraft.util.EnumFacing.UP;

public class BlockCandle extends BlockColored {

    public static final ResourceLocation NAME_BASE = new ResourceLocation(ModLib.MODID, "candle");

    public static ColoredGenerator GENERATOR = new ColoredGenerator(NAME_BASE) {
        @Override
        public Block createBlock(EnumDyeColor variant) {
            return new BlockCandle(variant);
        }
    };

    public static Block getBlock(EnumDyeColor color) {
        return getBlock(NAME_BASE,color);
    }

    public static Set<Block> getAll() {
        return Arrays.stream(ColorUtils.DYES).map(BlockCandle::getBlock).collect(Collectors.toSet());
    }

    public BlockCandle(EnumDyeColor color) {
        super(Material.GROUND, color);
    }

    public static ItemStack getStack(EnumDyeColor type) {
        return new ItemStack(getBlock(type));
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + 9 / 16d;
        double d2 = (double) pos.getZ() + 0.5D;
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.27D * (double) UP.getXOffset(), d1 + 0.22D, d2 + 0.27D * (double) UP.getZOffset(), 0.0D, 0.0D, 0.0D);
        worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, @Nonnull BlockPos pos) {
        BlockPos down = pos.down();
        BlockFaceShape blockfaceshape = worldIn.getBlockState(down).getBlockFaceShape(worldIn, down, UP);
        return blockfaceshape != BlockFaceShape.BOWL && blockfaceshape != BlockFaceShape.UNDEFINED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!canPlaceBlockAt(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return 15;
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing face) {
        return face == EnumFacing.DOWN ? BlockFaceShape.CENTER_SMALL : BlockFaceShape.UNDEFINED;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(7d / 16d, 0, 7d / 16d, 9d / 16d, 6d / 16d, 9d / 16d);
    }

    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

}
