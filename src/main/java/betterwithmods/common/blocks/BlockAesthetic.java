package betterwithmods.common.blocks;

import betterwithmods.lib.ModLib;
import betterwithmods.library.common.block.BlockEntryBuilderGenerator;
import betterwithmods.library.common.block.BlockTypeGenerator;
import betterwithmods.library.common.block.IBlockType;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static betterwithmods.common.blocks.BlockAesthetic.Type.HELLFIRE;

public class BlockAesthetic extends BWMBlock {
    private final Type type;

    public BlockAesthetic(Type type) {
        super(Material.ROCK);
        this.type = type;
    }

    public static Block getBlock(Type variant) {
        return ForgeRegistries.BLOCKS.getValue(variant.getRegistryName());
    }

    public static IBlockState getVariant(Type type) {
        return getBlock(type).getDefaultState();
    }

    public static ItemStack getStack(Type type) {
        return getStack(type, 1);
    }

    public static ItemStack getStack(Type type, int count) {
        return new ItemStack(getBlock(type), count);
    }

    @SuppressWarnings("deprecation")
    @Override
    public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos) {
        return type.getHardness();
    }

    @Nonnull
    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        return type.getSoundType();
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public Material getMaterial(IBlockState state) {
        return type.getMaterial();
    }

    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return type.getMapColor();
    }

    @Override
    public int getHarvestLevel(@Nonnull IBlockState state) {
        return 1;
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        return type.getResistance();
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }

    @Override
    public boolean isFireSource(@Nonnull World world, BlockPos pos, EnumFacing side) {
        return type == HELLFIRE;
    }

    public enum Type implements IBlockType {
        CHOPBLOCK("chopping_block", MapColor.STONE),
        CHOPBLOCKBLOOD("bloody_chopping_block", MapColor.NETHERRACK),
        NETHERCLAY("hardened_nether_clay", MapColor.NETHERRACK),
        HELLFIRE("hellfire_block", MapColor.ADOBE),
        ROPE("rope_block", MapColor.DIRT, Material.CLOTH, SoundType.CLOTH, 1F, 5F),
        FLINT("flint_block", MapColor.STONE),
        WHITESTONE("whitestone", MapColor.CLOTH),
        WHITECOBBLE("whitecobble", MapColor.CLOTH),
        ENDERBLOCK("ender_block", MapColor.CYAN),
        PADDING("padding_block", MapColor.CLOTH, Material.CLOTH, SoundType.CLOTH, 1F, 5F),
        SOAP("soap_block", MapColor.PINK, Material.GROUND, SoundType.GROUND, 1F, 5F),
        DUNG("dung_block", MapColor.BROWN, Material.GROUND, SoundType.GROUND, 1F, 2F),
        WICKER("wicker_block", MapColor.BROWN, Material.WOOD, SoundType.WOOD, 1F, 5F),
        NETHERCOAL("nethercoal_block", MapColor.RED, Material.REDSTONE_LIGHT, SoundType.STONE, 5.0F, 10.0F);

        private static final Type[] VALUES = values();

        private final String name;
        private final MapColor color;
        private final Material material;
        private final SoundType soundType;
        private final float hardness;
        private final float resistance;
        private final ResourceLocation registryName;

        Type(String name, MapColor color) {
            this(name, color, Material.ROCK, SoundType.STONE, 1.5F, 10F);
        }

        Type(String name, MapColor color, Material material, SoundType soundType, float hardness, float resistance) {
            this.name = name;
            this.color = color;
            this.material = material;
            this.soundType = soundType;
            this.hardness = hardness;
            this.resistance = resistance;
            this.registryName = new ResourceLocation(ModLib.MODID, name);
        }

        @Nonnull
        @Override
        public String getName() {
            return name;
        }

        @Override
        public ResourceLocation getRegistryName() {
            return registryName;
        }

        @Override
        public MapColor getMapColor() {
            return color;
        }

        @Override
        public Material getMaterial() {
            return material;
        }

        @Override
        public SoundType getSoundType() {
            return soundType;
        }

        @Override
        public float getHardness() {
            return hardness;
        }

        @Override
        public float getResistance() {
            return resistance;
        }
    }

    public static class Generator extends BlockTypeGenerator<Type> {

        public Generator() {
            super(Type.VALUES);
        }

        @Override
        public Block createBlock(Type variant) {
            return new BlockAesthetic(variant);
        }
    }
}
