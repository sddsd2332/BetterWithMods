package betterwithmods.testing.base.world;

import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import javax.annotation.Nullable;
import java.util.Map;

public class FakeWorld extends World {

    private final Map<BlockPos, IBlockState> states = Maps.newHashMap();

    public FakeWorld() {
        super(new FakeSaveHandler(), new FakeWorldInfo(), new FakeWorldProvider(), new Profiler(), false);
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        return null;
    }

    @Override
    public boolean setBlockState(BlockPos pos, IBlockState state) {
        this.states.put(pos, state);
        return true;
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        return states.getOrDefault(pos, Blocks.AIR.getDefaultState());
    }

    @Override
    protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
        return false;
    }

    @Nullable
    @Override
    public Entity getEntityByID(int id) {
        return null;
    }

    @Override
    public boolean setBlockToAir(BlockPos pos) {
        setBlockState(pos, Blocks.AIR.getDefaultState());
        return true;
    }

    @Override
    public boolean spawnEntity(Entity entityIn) {
        return true;
    }
}
