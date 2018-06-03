package betterwithmods.common.blocks.behaviors;

import betterwithmods.api.tile.dispenser.IBehaviorCollect;
import net.minecraft.block.Block;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Created by primetoxinz on 5/25/17.
 */
public class BehaviorBreakBlock implements IBehaviorCollect {
    @Override
    public NonNullList<ItemStack> collect(IBlockSource source) {
        Block block = source.getBlockState().getBlock();
        NonNullList<ItemStack> list = NonNullList.create();
        block.getDrops(list, source.getWorld(), source.getBlockPos(),source.getBlockState(), 0);
        breakBlock(source.getWorld(),source.getBlockState(),source.getBlockPos());
        return list;
    }
}
