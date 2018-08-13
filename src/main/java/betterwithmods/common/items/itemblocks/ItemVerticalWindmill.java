package betterwithmods.common.items.itemblocks;

import betterwithmods.client.tesr.TESRVerticalWindmill;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static net.minecraft.util.EnumFacing.Axis.Y;

public class ItemVerticalWindmill extends ItemAxleGenerator {

    public ItemVerticalWindmill(Block block) {
        super(block);
        this.radius = 4;
    }

    @Override
    public boolean isAxis(EnumFacing.Axis axis) {
        return axis.isVertical();
    }

    @Override
    public AxisAlignedBB getBounds(EnumFacing.Axis axis, int radius) {
        if (axis == Y) {
            return new AxisAlignedBB(-radius, -radius + 1, -radius, radius, radius - 1, radius);
        }
        return Block.NULL_AABB;
    }


    @Override
    public void renderModel(World world, BlockPos pos, double x, double y, double z, double partial) {
        TESRVerticalWindmill.renderWindmill(0, x, y, z, new int[]{0, 0, 0, 0, 0, 0, 0, 0});
    }


}
