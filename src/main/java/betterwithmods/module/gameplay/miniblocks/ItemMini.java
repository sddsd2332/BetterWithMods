package betterwithmods.module.gameplay.miniblocks;

import betterwithmods.module.gameplay.miniblocks.blocks.BlockMini;
import betterwithmods.module.gameplay.miniblocks.orientations.BaseOrientation;
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
