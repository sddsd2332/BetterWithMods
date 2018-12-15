package betterwithmods.module.recipes.miniblocks;

import betterwithmods.common.blocks.BlockChime;
import betterwithmods.common.blocks.BlockChimeIron;
import betterwithmods.common.blocks.BlockChimeReed;
import betterwithmods.common.blocks.camo.BlockDynamic;
import betterwithmods.common.tile.TileDynamic;
import betterwithmods.lib.ModLib;
import betterwithmods.module.recipes.miniblocks.blocks.*;
import betterwithmods.module.recipes.miniblocks.tiles.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Arrays;

public enum DynamicType {
    STAIR(BlockStair.class, TileStair.class, "stair"),
    BENCH(BlockBench.class, TileDynamic.class, "bench"),
    CORNER(BlockCorner.class, TileCorner.class, "corner"),
    COLUMN(BlockColumn.class, TileColumn.class, "column"),
    CHAIR(BlockChair.class, TileChair.class, "chair"),
    GRATE(BlockPane.class, TileDynamic.class, "grate"),
    MOULDING(BlockMoulding.class, TileMoulding.class, "moulding"),
    PEDESTAL(BlockPedestal.class, TilePedestal.class, "pedestal"),
    SIDING(BlockSiding.class, TileSiding.class, "siding"),
    SLATS(BlockPane.class, TileDynamic.class, "slats"),
    TABLE(BlockTable.class, TileDynamic.class, "table"),
    THINWALL(BlockPane.class, TileDynamic.class, "thinwall"),
    REED_CHIME(BlockChimeReed.class, TileDynamic.class, "reed_chime"),
    IRON_CHIME(BlockChimeIron .class, TileDynamic.class, "iron_chime");


    public static final DynamicType[] VALUES = values();
    private final Class<? extends BlockDynamic> block;
    private final Class<? extends TileEntity> tile;
    private final String name;

    DynamicType(Class<? extends BlockDynamic> block, Class<? extends TileEntity> tile, String name) {
        this.block = block;
        this.tile = tile;
        this.name = name;
    }

    public static boolean matches(DynamicType type, ItemStack stack) {
        return fromStack(stack).equals(type);
    }

    public static DynamicType fromName(String name) {
        return Arrays.stream(VALUES).filter(t -> t.isName(name)).findFirst().orElse(null);
    }

    public static DynamicType fromBlock(BlockDynamic block) {
        return Arrays.stream(VALUES).filter(t -> t.isBlock(block)).findFirst().orElse(null);
    }

    public static DynamicType fromStack(ItemStack stack) {
        if (stack.getItem() instanceof ItemCamo) {
            BlockDynamic mini = (BlockDynamic) ((ItemCamo) stack.getItem()).getBlock();
            return fromBlock(mini);
        }
        return null;
    }

    public static void registerTiles() {
        for (DynamicType type : VALUES) {
            if (type.tile != null) {
                if (TileEntity.getKey(type.tile) == null) {
                    GameRegistry.registerTileEntity(type.tile, new ResourceLocation(ModLib.MODID, type.name));
                }
            }
        }
    }

    private boolean isName(String name) {
        return this.name.equalsIgnoreCase(name);
    }

    private boolean isBlock(BlockDynamic mini) {
        return this.block.isAssignableFrom(mini.getClass());
    }

    public Class<? extends BlockDynamic> getBlock() {
        return block;
    }

    public Class<? extends TileEntity> getTile() {
        return tile;
    }

    public String getName() {
        return name;
    }
}
