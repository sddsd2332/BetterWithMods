package betterwithmods.common.items.itemblocks;

import betterwithmods.client.tesr.TESRWaterwheel;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemWaterwheel extends ItemAxleGenerator {

    public ItemWaterwheel(Block block) {
        super(block);
        this.radius = 2;
    }

    @Override
    public boolean isAxis(EnumFacing.Axis axis) {
        return axis.isHorizontal();
    }

    @Override
    public void renderModel(World world, BlockPos pos, double x, double y, double z, double partial) {
        EnumFacing.Axis axis = getAxleAxis(world, pos);
        TESRWaterwheel.renderWaterwheel(axis == EnumFacing.Axis.Z ? 180 : 90, 0, x, y, z);
    }

}
