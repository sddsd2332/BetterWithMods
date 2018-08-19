package betterwithmods.module.recipes.miniblocks;

import betterwithmods.module.recipes.miniblocks.blocks.BlockMini;
import betterwithmods.module.recipes.miniblocks.orientations.BaseOrientation;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemMini extends ItemCamo {
    public ItemMini(Block block) {
        super(block);
    }

    public BaseOrientation getDefaultOrientation(ItemStack stack) {
        return ((BlockMini) getBlock()).getDefaultOrientation(stack);
    }

}
