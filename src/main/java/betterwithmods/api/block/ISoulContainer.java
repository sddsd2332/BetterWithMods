package betterwithmods.api.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISoulContainer {
    int getMaxSouls();
    default boolean onFull(World world, BlockPos pos) {
        return true;
    }
}
