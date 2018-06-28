package betterwithmods.module.stronghold;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStronghold;

import javax.annotation.Nonnull;
import java.util.Map;

public class GenStronghold extends MapGenStronghold {
    public GenStronghold() {
    }

    public GenStronghold(Map<String, String> p_i2068_1_) {
    }

    @Override
    public BlockPos getNearestStructurePos(World worldIn, @Nonnull BlockPos pos, boolean findUnexplored) {
        return new BlockPos(0,0,0);
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        return super.canSpawnStructureAtCoords(chunkX, chunkZ);
    }
}
