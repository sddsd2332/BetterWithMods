package betterwithmods.api.util;

import net.minecraft.item.ItemStack;

public interface IBlockVariants {

    ItemStack getVariant(EnumBlock type, int count);

    IBlockVariants addVariant(EnumBlock type, ItemStack stack);

    enum EnumBlock {
        LOG,
        BLOCK,
        BARK,
        SAWDUST,
        STAIR,
        FENCE,
        FENCE_GATE,
        WALL
    }
}
