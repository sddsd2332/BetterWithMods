package betterwithmods.common.blocks;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
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

/**
 * Created by blueyu2 on 11/19/16.
 */
public class BlockRawPastry extends Block {

    public static final HashMap<EnumType, BlockRawPastry> BLOCKS = Maps.newHashMap();
    private final EnumType type;

    public BlockRawPastry(EnumType type) {
        super(Material.CAKE);
        this.setDefaultState(this.blockState.getBaseState());
        this.setHardness(0.1F);
        this.setSoundType(SoundType.CLOTH);

        this.setRegistryName(type.getName());
        this.type = type;
    }

    public static void init() {
        for (EnumType type : EnumType.VALUES) {
            BLOCKS.put(type, new BlockRawPastry(type));
        }
    }

    public static ItemStack getStack(EnumType type) {
        return new ItemStack(BLOCKS.get(type));
    }

    @Nonnull
    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.NORMAL;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, @Nonnull BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!this.canBlockStay(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return type.getAABB();
    }

    private boolean canBlockStay(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos, EnumFacing.UP);
    }

    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face != EnumFacing.DOWN ? BlockFaceShape.UNDEFINED : BlockFaceShape.CENTER;
    }

    public enum EnumType implements IStringSerializable {
        CAKE("raw_cake", new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D)),
        PUMPKIN("raw_pumpkin_pie", new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D)),
        COOKIE("raw_cookie", new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D)),
        BREAD("raw_flour", new AxisAlignedBB(0.25D, 0.0D, 0.0625D, 0.75D, 0.375D, 0.9375D)),
        APPLE("raw_apple_pie", new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D)),
        MELON("raw_melon_pie", new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D));
        public static final BlockRawPastry.EnumType[] VALUES = values();

        private final String name;
        private final AxisAlignedBB aabb;

        EnumType(String nameIn, AxisAlignedBB aabbIn) {
            this.name = nameIn;
            this.aabb = aabbIn;
        }


        @Nonnull
        @Override
        public String getName() {
            return this.name;
        }

        public AxisAlignedBB getAABB() {
            return this.aabb;
        }

        public String toString() {
            return this.name;
        }
    }
}
