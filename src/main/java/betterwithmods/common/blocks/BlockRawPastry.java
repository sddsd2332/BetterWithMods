package betterwithmods.common.blocks;

import betterwithmods.lib.ModLib;
import betterwithmods.library.common.block.BlockTypeGenerator;
import betterwithmods.library.common.block.IBlockType;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
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
import java.util.HashMap;

/**
 * Created by blueyu2 on 11/19/16.
 */
public class BlockRawPastry extends Block {

    private final Type type;

    private BlockRawPastry(Type type) {
        super(Material.CAKE);
        this.setDefaultState(this.blockState.getBaseState());
        this.setHardness(0.1F);
        this.setSoundType(SoundType.CLOTH);
        this.type = type;
    }

    public static Block getBlock(Type type) {
        return ForgeRegistries.BLOCKS.getValue(type.getRegistryName());
    }


    public static ItemStack getStack(Type type) {
        return new ItemStack(getBlock(type));
    }


    @Nonnull
    @Override
    public EnumPushReaction getPushReaction(IBlockState state) {
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

    public enum Type implements IBlockType {
        CAKE("raw_cake", new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D)),
        PUMPKIN("raw_pumpkin_pie", new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D)),
        COOKIE("raw_cookie", new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.0625D, 0.8125D)),
        BREAD("raw_flour", new AxisAlignedBB(0.25D, 0.0D, 0.0625D, 0.75D, 0.375D, 0.9375D)),
        APPLE("raw_apple_pie", new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D)),
        MELON("raw_melon_pie", new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.5D, 0.9375D));
        public static final Type[] VALUES = values();

        private final String name;
        private final AxisAlignedBB aabb;
        private final ResourceLocation registryName;

        Type(String name, AxisAlignedBB aabbIn) {
            this.name = name;
            this.aabb = aabbIn;
            this.registryName = new ResourceLocation(ModLib.MODID, name);
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
            return new BlockRawPastry(variant);
        }
    }
}
