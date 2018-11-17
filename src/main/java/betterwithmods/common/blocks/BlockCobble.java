package betterwithmods.common.blocks;

import betterwithmods.lib.ModLib;
import betterwithmods.library.common.block.IBlockType;
import betterwithmods.library.common.block.creation.BlockTypeGenerator;
import betterwithmods.library.utils.GlobalUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockCobble extends Block {
    private final Type type;

    public BlockCobble(Type type) {
        super(Material.ROCK);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.type = type;
    }

    public static Block getBlock(Type type) {
        return ForgeRegistries.BLOCKS.getValue(type.getRegistryName());
    }

    public static ItemStack getStack(Type type) {
        return new ItemStack(getBlock(type));
    }

    public static Set<Block> getAll() {
        return Arrays.stream(Type.VALUES).map(BlockCobble::getBlock).collect(Collectors.toSet());
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return type.getMapColor();
    }

    public enum Type implements IBlockType {
        GRANITE("cobblestone_granite", Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE)),
        DIORITE("cobblestone_diorite", Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE)),
        ANDESITE("cobblestone_andesite", Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE));

        public static final Type[] VALUES = values();
        private final ResourceLocation registryName;
        private final IBlockState state;

        Type(String name, IBlockState state) {
            this.registryName = new ResourceLocation(ModLib.MODID, name);
            this.state = state;
        }

        public static Type convert(BlockStone.EnumType type) {
            return Type.valueOf(type.name());
        }

        public IBlockState getState() {
            return this.state;
        }

        public ItemStack getStack() {
            return GlobalUtils.getStackFromState(state);
        }

        @Nonnull
        @Override
        public ResourceLocation getRegistryName() {
            return registryName;
        }

        @Override
        public String getName() {
            return getRegistryName().toString();
        }

    }

    public static class Generator extends BlockTypeGenerator<Type> {

        public Generator() {
            super(Type.VALUES);
        }

        @Override
        public Block createBlock(Type variant) {
            return new BlockCobble(variant);
        }
    }

}
