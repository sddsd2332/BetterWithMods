package betterwithmods.common.blocks;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class BlockUnfiredPottery extends BWMBlock {

    public static final HashMap<EnumType, Block> BLOCKS = Maps.newHashMap();
    private static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    private static final AxisAlignedBB URN_AABB = new AxisAlignedBB(0.3125D, 0.0F, 0.3125D, 0.6875D, 0.625D, 0.6875D);
    private static final AxisAlignedBB VASE_AABB = new AxisAlignedBB(0.125D, 0, 0.125D, 0.875D, 1.0D, 0.875D);
    private static final AxisAlignedBB BRICK_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.0625D, 0.75D, 0.375D, 0.9375D);
    private final EnumType type;

    public BlockUnfiredPottery(EnumType type) {
        super(Material.CLAY);
        this.setSoundType(SoundType.GROUND);
        this.setHardness(0.5F);
        this.setRegistryName("unfired_" + type.getName());
        this.type = type;
    }

    public static void init() {
        for (EnumType type : EnumType.VALUES) {
            BLOCKS.put(type, new BlockUnfiredPottery(type));
        }
    }

    public static ItemStack getStack(EnumType type) {
        return new ItemStack(BLOCKS.get(type));
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

    public enum EnumType implements IStringSerializable {
        CRUCIBLE(0, "crucible", BLOCK_AABB),
        PLANTER(1, "planter", BLOCK_AABB),
        URN(2, "urn", URN_AABB),
        VASE(3, "vase", VASE_AABB),
        BRICK(4, "brick", BRICK_AABB),
        NETHER_BRICK(5, "nether_brick", BRICK_AABB);
        private static final EnumType[] VALUES = values();

        private final String name;
        private final int meta;
        private final AxisAlignedBB bounds;

        EnumType(int meta, String name, AxisAlignedBB bounds) {
            this.meta = meta;
            this.name = name;
            this.bounds = bounds;
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
    }
}
