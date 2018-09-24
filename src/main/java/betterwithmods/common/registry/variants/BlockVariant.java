package betterwithmods.common.registry.variants;

import betterwithmods.api.util.IBlockVariants;
import betterwithmods.library.utils.InventoryUtils;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class BlockVariant implements IBlockVariants {
    private final HashMap<EnumBlock, ItemStack> variants = Maps.newHashMap();

    public static BlockVariant builder() {
        return new BlockVariant();
    }

    @Override
    public ItemStack getVariant(EnumBlock type, int count) {

        return InventoryUtils.setCount(variants.getOrDefault(type, ItemStack.EMPTY), count);
    }

    @Override
    public BlockVariant addVariant(EnumBlock type, ItemStack stack) {
        variants.put(type, stack.copy());
        return this;
    }

}