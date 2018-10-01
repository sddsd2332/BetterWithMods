package betterwithmods.common.blocks;

import betterwithmods.lib.ModLib;
import betterwithmods.library.common.block.BlockTypeGenerator;
import betterwithmods.library.common.block.IBlockType;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;

public class BlockUnfiredPottery extends BWMBlock {


    private static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    private static final AxisAlignedBB URN_AABB = new AxisAlignedBB(0.3125D, 0.0F, 0.3125D, 0.6875D, 0.625D, 0.6875D);
    private static final AxisAlignedBB VASE_AABB = new AxisAlignedBB(0.125D, 0, 0.125D, 0.875D, 1.0D, 0.875D);
    private static final AxisAlignedBB BRICK_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.0625D, 0.75D, 0.375D, 0.9375D);
    private final Type type;

    public BlockUnfiredPottery(Type type) {
        super(Material.CLAY);
        this.setSoundType(SoundType.GROUND);
        this.setHardness(0.5F);
        this.type = type;
    }

    public static Block getBlock(Type type) {
        return ForgeRegistries.BLOCKS.getValue(type.getRegistryName());
    }

    public static ItemStack getStack(Type type) {
        return new ItemStack(getBlock(type));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face != EnumFacing.DOWN ? BlockFaceShape.UNDEFINED : BlockFaceShape.CENTER;
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {
        return world.isSideSolid(pos.down(), EnumFacing.UP);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        if (!world.isSideSolid(pos.down(), EnumFacing.UP)) {
            dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return type.getBounds();
    }

    public enum Type implements IBlockType {
        CRUCIBLE(0, "crucible", BLOCK_AABB),
        PLANTER(1, "planter", BLOCK_AABB),
        URN(2, "urn", URN_AABB),
        VASE(3, "vase", VASE_AABB),
        BRICK(4, "brick", BRICK_AABB),
        NETHER_BRICK(5, "nether_brick", BRICK_AABB);
        private static final Type[] VALUES = values();

        private final String name;
        private final int meta;
        private final AxisAlignedBB bounds;
        private final ResourceLocation registryName;

        Type(int meta, String name, AxisAlignedBB bounds) {
            this.meta = meta;
            this.name = name;
            this.bounds = bounds;
            this.registryName = new ResourceLocation(ModLib.MODID, "unfired_" + name);
        }

        public AxisAlignedBB getBounds() {
            return bounds;
        }

        public int getMeta() {
            return meta;
        }

        @Nonnull
        @Override
        public String getName() {
            return name;
        }

        @Nonnull
        @Override
        public ResourceLocation getRegistryName() {
            return registryName;
        }
    }

    public static class Generator extends BlockTypeGenerator<Type> {
        public Generator() {
            super(Type.VALUES);
        }

        @Override
        public Block createBlock(Type variant) {
            return new BlockUnfiredPottery(variant);
        }
    }
}
