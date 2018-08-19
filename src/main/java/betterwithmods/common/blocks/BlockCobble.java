package betterwithmods.common.blocks;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class BlockCobble extends Block {
    public static final HashMap<EnumType, BlockCobble> BLOCKS = Maps.newHashMap();
    public final BlockCobble.EnumType type;

    public BlockCobble(BlockCobble.EnumType type) {
        super(Material.ROCK);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setTranslationKey("bwm:cobble");
        this.setRegistryName("cobblestone_" + type.getName());
        this.type = type;
    }

    public static void init() {
        for (BlockCobble.EnumType type : EnumType.VALUES)
            BLOCKS.put(type, new BlockCobble(type));
    }

    public static ItemStack getStack(BlockCobble.EnumType type) {
        return new ItemStack(BLOCKS.get(type));
    }


    @Nonnull
    @SuppressWarnings("deprecation")
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return type.getColor();
    }

    public enum EnumType implements IStringSerializable {
        GRANITE("granite", new ItemStack(Blocks.STONE, 1, 1), MapColor.DIRT),
        DIORITE("diorite", new ItemStack(Blocks.STONE, 1, 3), MapColor.QUARTZ),
        ANDESITE("andesite", new ItemStack(Blocks.STONE, 1, 5), MapColor.STONE);

        private static final EnumType[] VALUES = values();
        private final String name;
        private final MapColor color;
        private final ItemStack stone;

        EnumType(String name, ItemStack stone, MapColor color) {
            this.name = name;
            this.stone = stone;
            this.color = color;
        }

        public ItemStack getStone() {
            return stone;
        }

        @Nonnull
        @Override
        public String getName() {
            return name;
        }

        public MapColor getColor() {
            return color;
        }

        public static EnumType convert(BlockStone.EnumType type) {
            switch (type) {
                case GRANITE:
                    return GRANITE;
                case DIORITE:
                    return DIORITE;
                case ANDESITE:
                    return ANDESITE;
            }
            return null;
        }
    }
}
