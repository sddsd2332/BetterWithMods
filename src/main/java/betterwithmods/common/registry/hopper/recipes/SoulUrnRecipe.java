package betterwithmods.common.registry.hopper.recipes;

import betterwithmods.common.tile.TileFilteredHopper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class SoulUrnRecipe extends HopperRecipe {
    public SoulUrnRecipe(Ingredient input, ItemStack output, ItemStack... secondaryOutput) {
        super("betterwithmods:soul_sand", input, output, secondaryOutput);
    }

    public SoulUrnRecipe(Ingredient input, List<ItemStack> output, List<ItemStack> secondaryOutput) {
        super("betterwithmods:soul_sand", input, output, secondaryOutput);
    }

    @Override
    public void onCraft(World world, BlockPos pos, EntityItem item, TileFilteredHopper tile) {
        tile.increaseSoulCount(1);
        if (!world.isRemote) {
            world.playSound(null, pos, SoundEvents.ENTITY_GHAST_AMBIENT, SoundCategory.BLOCKS, 1.0F, world.rand.nextFloat() * 0.1F + 0.45F);
        }
        super.onCraft(world, pos, item, tile);
    }
}