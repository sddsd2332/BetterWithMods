package betterwithmods.module.gameplay.miniblocks;

import betterwithmods.BWMod;
import betterwithmods.common.blocks.camo.BlockCamo;
import betterwithmods.common.tile.TileCamo;
import betterwithmods.module.gameplay.miniblocks.blocks.*;
import betterwithmods.module.gameplay.miniblocks.tiles.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;

public enum MiniType {
    SIDING(BlockSiding.class, TileSiding.class, "siding"),
    MOULDING(BlockMoulding.class, TileMoulding.class, "moulding"),
    CORNER(BlockCorner.class, TileCorner.class, "corner"),
    COLUMN(BlockColumn.class, TileColumn.class, "column"),
    PEDESTAL(BlockPedestals.class, TilePedestal.class, "pedestal"),
    TABLE(BlockTable.class, TileCamo.class, "table"),
    BENCH(BlockBench.class, TileCamo.class, "bench"),
    CHAIR(BlockChair.class, TileChair.class, "chair"),
    STAIR(BlockStair.class, TileStair.class, "stair"),
    UNKNOWN(null, null, "");

    public static final MiniType[] VALUES = values();

    private final Class<? extends BlockCamo> block;
    private final Class<? extends TileEntity> tile;
    private final String name;

    @SideOnly(Side.CLIENT)
    MiniType(Class<? extends BlockCamo> block, Class<? extends TileEntity> tile, String name) {
        this.block = block;
        this.tile = tile;
        this.name = name;
    }

    public static boolean matches(MiniType type, ItemStack stack) {
        return fromStack(stack).equals(type);
    }

    public static MiniType fromName(String name) {
        return Arrays.stream(VALUES).filter(t -> t.isName(name)).findFirst().orElse(null);
    }

    public static MiniType fromBlock(BlockCamo block) {
        return Arrays.stream(VALUES).filter(t -> t.isBlock(block)).findFirst().orElse(null);
    }

    public static MiniType fromStack(ItemStack stack) {
        if (stack.getItem() instanceof ItemCamo) {
            BlockCamo mini = (BlockCamo) ((ItemCamo) stack.getItem()).getBlock();
            return fromBlock(mini);
        }
        return UNKNOWN;
    }

    public static void registerTiles() {
        for (MiniType type : VALUES) {
            if (type.tile != null) {
                if (TileEntity.getKey(type.tile) == null) { //TODO - datafixer for conversion
                    GameRegistry.registerTileEntity(type.tile, new ResourceLocation(BWMod.MODID, type.name));
                }
            }
        }
    }

    private boolean isName(String name) {
        return this.name.equalsIgnoreCase(name);
    }

    private boolean isBlock(BlockCamo mini) {
        return this.block.isAssignableFrom(mini.getClass());
    }
}
