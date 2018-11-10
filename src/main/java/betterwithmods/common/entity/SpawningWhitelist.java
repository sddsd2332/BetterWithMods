package betterwithmods.common.entity;

import betterwithmods.library.utils.ingredient.blockstate.BlockStateIngredient;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class SpawningWhitelist {
    private final List<BlockStateIngredient> whitelist = Lists.newArrayList();

    public void addIngredient(BlockStateIngredient ingredient) {
        whitelist.add(ingredient);
    }

    public void addBlock(Block block) {
        whitelist.add(new BlockStateIngredient(Ingredient.fromItem(Item.getItemFromBlock(block))));
    }

    public void addBlock(ItemStack stack) {
        whitelist.add(new BlockStateIngredient(stack));
    }

    public boolean isBlacklisted(World world, BlockPos pos, IBlockState state) {
        return whitelist.stream().noneMatch(i -> i.test(world, pos, state));
    }
}