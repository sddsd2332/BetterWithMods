package betterwithmods.common.tile;

import betterwithmods.common.BWMBlocks;
import betterwithmods.util.DirUtils;
import betterwithmods.util.WorldUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileWindmillHorizontal extends TileBaseWindmill {

    public TileWindmillHorizontal() {
        super(4);
    }

    @Override
    public void verifyIntegrity() {
        boolean valid = true;
        if (getBlockWorld().getBlockState(pos).getBlock() == BWMBlocks.HORIZONTAL_WINDMILL) {
            EnumFacing.Axis axis = getBlockWorld().getBlockState(pos).getValue(DirUtils.AXIS);
            for (int vert = -6; vert <= 6; vert++) {
                for (int i = -6; i <= 6; i++) {
                    int xP = (axis == EnumFacing.Axis.Z ? i : 0);
                    int zP = (axis == EnumFacing.Axis.X ? i : 0);
                    BlockPos offset = pos.add(xP, vert, zP);
                    if (xP == 0 && vert == 0 && zP == 0)
                        continue;
                    else {
                        IBlockState state = world.getBlockState(offset);
                        valid = state.getBlock().isReplaceable(world, offset);
                    }
                    if (!valid)
                        break;
                }
                if (!valid)
                    break;
            }
        }
        isValid = valid && this.getBlockWorld().canBlockSeeSky(pos) && !WorldUtils.isNether(world) && !WorldUtils.isTheEnd(world);
    }

    @Override
    public int getRadius() {
        return 7;
    }



}
