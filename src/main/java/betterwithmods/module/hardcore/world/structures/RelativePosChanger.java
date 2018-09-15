package betterwithmods.module.hardcore.world.structures;

import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class RelativePosChanger implements IChanger {
    private Set<BlockPos> check;
    private IBlockState state;

    public RelativePosChanger(IBlockState state, AxisAlignedBB box) {
        this.check = Sets.newHashSet(getPosInBox(box));
        this.state = state;
    }

    public RelativePosChanger(IBlockState state, BlockPos... positions) {
        this.check = Sets.newHashSet(positions);
        this.state = state;
    }

    @Override
    public boolean canChange(World world, BlockPos pos, BlockPos relativePos, IBlockState original) {
        return check.contains(relativePos);
    }

    @Override
    public IBlockState change(World world, BlockPos pos, BlockPos relativePos, IBlockState original) {
        return state;
    }

    public static Iterable<BlockPos> getPosInBox(AxisAlignedBB box) {
        return BlockPos.getAllInBox(new BlockPos((int)box.minX,(int) box.minY, (int)box.minZ), new BlockPos((int) (box.maxX), (int) (box.maxY), (int) (box.maxZ)));
    }
}
