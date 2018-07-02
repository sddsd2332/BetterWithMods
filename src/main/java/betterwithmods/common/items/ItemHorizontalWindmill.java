package betterwithmods.common.items;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;

public class ItemHorizontalWindmill extends ItemAxleBase {

    public ItemHorizontalWindmill(Block block) {
        super(block);
        this.radius = 6;
    }

    @Override
    public boolean isAxis(EnumFacing.Axis axis) {
        return axis.isHorizontal();
    }

    @Override
    public String tooltip() {
        return I18n.format("bwm.tooltip.horizontal_windmill.name");
    }



}
