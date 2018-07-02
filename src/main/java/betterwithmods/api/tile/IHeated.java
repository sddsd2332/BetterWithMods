package betterwithmods.api.tile;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IHeated extends IBulkTile {
    int getHeat(World world, BlockPos pos);
}
