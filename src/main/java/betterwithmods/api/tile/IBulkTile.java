package betterwithmods.api.tile;

import betterwithmods.api.recipe.input.IRecipeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public interface IBulkTile extends IRecipeContext {
    World getWorld();
    BlockPos getPos();
    ItemStackHandler getInventory();
}
