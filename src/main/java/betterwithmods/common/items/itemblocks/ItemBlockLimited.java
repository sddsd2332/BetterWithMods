package betterwithmods.common.items.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockLimited extends ItemBlock {
    private final int stackLimit;

    public ItemBlockLimited(Block block, int stackLimit) {
        super(block);
        this.stackLimit = stackLimit;
        this.setMaxDamage(0).setHasSubtypes(true);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return stackLimit;
    }
}