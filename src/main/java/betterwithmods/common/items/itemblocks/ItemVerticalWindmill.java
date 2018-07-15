package betterwithmods.common.items.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemVerticalWindmill extends ItemAxleBase {

    public ItemVerticalWindmill(Block block) {
        super(block);
        this.radius = 4;
    }

    @Override
    public boolean isAxis(EnumFacing.Axis axis) {
        return axis.isVertical();
    }

    @Override
    public String tooltip() {
        return I18n.format("bwm.tooltip.vertical_windmill.name");
    }

    @Override
    public AxisAlignedBB getBounds(EnumFacing.Axis axis, int radius) {
        switch (axis) {
            case Y:
                return new AxisAlignedBB(-radius, -radius + 1, -radius, radius, radius - 1, radius);
            default:
                return Block.NULL_AABB;
        }

    }

    @Override
    public void render(World world, Block block, BlockPos pos, ItemStack stack, EntityPlayer player, EnumFacing side, RayTraceResult target, double partial) {

    }
}
