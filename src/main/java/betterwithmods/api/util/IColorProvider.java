package betterwithmods.api.util;

import net.minecraft.item.ItemStack;

public interface IColorProvider {
    int getColor(ItemStack stack);
}
