package betterwithmods.api.tile;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public interface IBulkTile {
    World getWorld();
    BlockPos getPos();
    ItemStackHandler getInventory();
}
